#workflow document interpreter
## constants
import string
import cPickle
#first the supporting functions.
class RuleStore:
        """maintains set of rules"""
        def __init__(self):
                self.rules= []
        def add(self,rule):
                """add a rule to the store"""
                self.rules.append(rule)
        def findTriggeredRule(self,activityStatusMap):
            for rule in self.rules:
                        if rule.isTriggered(activityStatusMap):
                                return rule
            return None
        def addParallelBranch(self,branchIndex,varName,varVal,stateList):
                    """ find all rules referencing any states in statelist, create copies with references mangled"""
                    newRules = []
                    for state in stateList:
                            for rule in self.rules:
                                    if rule.references(state):
                                                newRules.append(rule.rewriteAs(state,state+ ":" + str(branchIndex)))
        def addEndRule(self,key,branchNameFn,count):
                """construct an parfor end rule from the info supplied, add it to store"""
                triggerList = map(lambda x: "_getStatus('" + branchNameFn(x) + "')==FINISHED", range(count))
                body1 = "_setStatus('" + key + "',FINISHED)"
                envList = map (branchNameFn,range(count))
                body2 = "_setEnv('" + key + "', _mergeEnvs(" + str(envList) +"))"
                endRule = Rule(reduce(lambda a, b: a + " and " + b, triggerList),[body1,body2])
                self.add(endRule)


class Rule :
        """represents a rule that is fired - has a trigger, location of environment, and list of actions to execute"""
        def __init__(self,name,trigger,bodyList=[],envId=None):
                self.trigger = string.strip(trigger)
                self.name = string.strip(name)
                self.envId = envId
                self.bodyList = bodyList
        def isTriggered(self,activityStatusMap):
                        """returns true if trigger of rule succeeds"""
                        triggerGlobals = mkTriggerGlobals(activityStatusMap)
                        return eval(self.trigger,triggerGlobals,{})
        def fire(self,activityStatusMap,ruleStore):
                        """execute the bodies of a rule"""
                        _jes.log.info("Firing " + self.name)
                        actionGlobals = mkActionGlobals(activityStatusMap,ruleStore)
                        if self.envId == None:
                            env = {}
                        else:
                            env = activityStatusMap.getEnv(self.envId)
                        for body in self.bodyList:
                                b = dedent(body)
                                _jes.log.debug( b )
                                exec b in actionGlobals, env
        def references(self,key):
                    """returns true if any trigger, env or body action references this key"""
                    if self.envId == key:
                        return true
                    test = lambda code: string.find(code,"'" + key + "'") != -1 or string.find(code,'"' + key + '"') != -1
                    if test(self.trigger):
                        return true
                    for body in self.bodyList:
                            if test(body):
                                    return true
                    return false
        def rewriteAs(oldKey,newKey):
                """create a copy of this rule, rewritten so that all referneces to old key reference new key"""
                replace = lambda code: string.replace(string.replace(code,"'" + oldKey + "'", "'" + newKey + "'"),'"' + oldKey + '"', '"' + newKey + '"')
                if self.envId == oldKey:
                        newEnvId = newKey
                else:
                        newEnvId = oldKey
                return Rule(self.name + " branch " + newKey,replace(self.trigger),map(replace,self.bodyList),newEnvId)

class ActivityStatusMap:
        """maintains current state of each activity in the workflow"""
        def __init__(self):
                self.states = {}
        def __getActivityStatus(self,key):
                """get state from map, if present, else creates one"""
                return self.states.setdefault(key,ActivityStatus(key))
        def getStatus(self,key):
                """returns the status code associated with an activity key"""
                return self.__getActivityStatus(key).status
        def setStatus(self,key,stat):
                """set the status code for an activity key"""
                self.__getActivityStatus(key).status = stat
                _jes.setStatus(key,stat);
        def getEnv(self,key):
                """get the environment associated with an acitivity key - may be {}"""
                return self.__getActivityStatus(key).env
        def setEnv(self,key,env):
                """set the environment associated with an activity key"""
                self.__getActivityStatus(key).env = env
        def cloneEnv(self,env):
                return env.copy()
        def mergeEnvs(self,envList):
                """merge environments"""
                def fn(a, b):
                        a.update(b)
                        return a
                return reduce(fn,map(self.getEnv,envList))

class ActivityStatus:
        """maintains status of a single activity, and possibly its associated environment"""
        def __init__(self,key,env={}):
                self.key = key
                self.status = 'UNSTARTED'
                self.env = env

class Interpreter:
        """top level class"""
        def __init__(self):
                self.ruleStore = RuleStore()
                self.stateMap = ActivityStatusMap()
        def addRule(self, rule):
                _jes.log.debug("adding rule " + rule.name)
                self.ruleStore.add(rule)
        def findNext(self):
                """find next candidate"""
                return self.ruleStore.findTriggeredRule(self.stateMap)
        def runNext(self):
                rule = self.findNext()
                _jes.log.debug("found rule " + rule.name + " to be run next");
                rule.fire(self.stateMap,self.ruleStore)
        def run(self):
                """ run the interpreter, until no more rules can be triggered """
                rule = self.ruleStore.findTriggeredRule(self.stateMap)
                while rule != None:
                        _jes.log.debug("found rule " + rule.name + " to be run next");
                        rule.fire(self.stateMap,self.ruleStore)
                        rule = self.ruleStore.findTriggeredRule(self.stateMap)
        def updateStatus(self,key,state):
                """alter the status associated with an activity key"""
                self.stateMap.setStatus(key,state)
        def pickle(self,stream):
                        _jes.log.debug("Pickling self..");
                        cPickle.dump(self,stream)



## supporting methods
def dedent(text):
    """helper funciton to clean up input script actions - as may have extra whitespace to left, which python is fussy about."""
    lines = text.expandtabs().split('\n')
    margin = None
    for line in lines:
        content = line.lstrip()
        if not content:
            continue
        indent = len(line) - len(content)
        if margin is None:
            margin = indent
        else:
            margin = min(margin, indent)

    if margin is not None and margin > 0:
        for i in range(len(lines)):
            lines[i] = lines[i][margin:]

    return '\n'.join(map(string.rstrip,filter(lambda x: string.strip(x) != "",lines)))

def mkTriggerGlobals(activityStatusMap):
        """define the namespace used to evaluate triggers in"""
        env =  {"_getStatus":activityStatusMap.getStatus
                        ,'UNSTARTED': "UNSTARTED"
                        ,'START' : "START"
                        ,'STARTED' : "STARTED"
                        ,'FINISH' : "FINISH"
                        ,'FINISHED' : "FINISHED"
                        ,'ERROR' : "ERROR"
                        }
        env.update(globals())
        return env;
        
def dispatchStep(id) :
	"""evaluate parameters to step, before passing into java-method"""
	pass
	
def completeStep(id):
   """call java method, and then copy parameters back out again"""
   pass
   
def mkActionGlobals(activityStatusMap, ruleStore):
        """define the global namespace used to execute body actions"""
        env = mkTriggerGlobals(activityStatusMap)
        env['_setStatus'] = activityStatusMap.setStatus
        env['_getEnv'] = activityStatusMap.getEnv
        env['_setEnv'] = activityStatusMap.setEnv
        env['_cloneEnv'] = activityStatusMap.cloneEnv
        env['_mergeEnvs'] = activityStatusMap.mergeEnvs
        env['_addParallelBranch'] = ruleStore.addParallelBranch
        env['_addEndRule'] = ruleStore.addEndRule
        env['_dispatchStep'] = _jes.dispatchStepWithId
        env['_completeStep'] = _jes.completeStepWithId
        return env




