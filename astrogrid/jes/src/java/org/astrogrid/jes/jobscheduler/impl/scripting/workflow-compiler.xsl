<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
	xmlns:wf="http://www.astrogrid.org/schema/AGWorkflow/v1"
	xmlns:cred="http://www.astrogrid.org/schema/Credentials/v1"
	xmlns:agpd="http://www.astrogrid.org/schema/AGParameterDefinition/v1"	
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"	
	>
<!-- this stylsheet translates an extended-workflow document into a 
document of rules. these are then further processed and translated to the interpreter used

format of a rule element:
<rule trigger="boolean-expression" name="textual name" env="key">
  <action>
    script statements
  </action>
</rule>

the trigger attribute (required) should be a boolean expression with no side effects - as its going to be 
evaluated frequently. when it evaluates to true, the rule is able to fire.

the name attribute (optional) of the rule is used for documentation and logging

the env attribute (optional) gives a key to a state. the environment stored in this state
is the environment the body of the rule should be executed in. if no env is declared, then
the body of the rule is executed in the default environment.

a rule contains a sequence of <action> element, each of which contains script instructions.
they are executed in order by the script interpreter.

the script interpreter is required to provide the following methods in the default environment
_setStatus - set status of a state
_getStatus - get status of a state.
_getEnv - get the environment from a state
_setEnv - set the environment for a state.
_cloneEnvs - produce a shallow copy of an environment
_mergeEnvs - merge a list of environments into a single environment
and the constants UNSTARTED, START, STARTED, FINISH, FINISHED, ERROR
Finally, the implementation of parfor requires two further methods, to add dynamically-generated rules into the rulebase.
_addParallelBranch
_addEndRule

tranalation makes a heavy use of the xslt-xpath function generate-id(), which generates a unique, constant key for an element in 
the document. this is used as the symbolic name of the activity. actually, xslt is really nice for crawling around 'syntax' trees,
as it allows you to travese and select on all the different xpath axis... 
-->


<xsl:output method="xml" indent="yes" />
<xsl:template match="/">
	<xsl:apply-templates select="wf:workflow" />
</xsl:template>
<!-- what to do with other stuff... - drop it -->
<xsl:template match="text()|@*" />

<!-- rule to handle castor returning base type elements -->
<xsl:template match="wf:Activity">
		<xsl:choose>
			<xsl:when test="@xsi:type = 'sequence'">
				<xsl:call-template name="sequence" />
			</xsl:when>
			<xsl:when test="@xsi:type = 'flow'">
				<xsl:call-template name="flow" />
			</xsl:when>
			<xsl:when test="@xsi:type = 'step'">
				<xsl:call-template name="step" />
			</xsl:when>
			<xsl:when test="@xsi:type = 'script'">
				<xsl:call-template name="script" />
			</xsl:when>
			<xsl:when test="@xsi:type = 'for'">
				<xsl:call-template name="for" />
			</xsl:when>
			<xsl:when test="@xsi:type = 'while'">
				<xsl:call-template name="while" />
			</xsl:when>
			<xsl:when test="@xsi:type = 'parfor'">
				<xsl:call-template name="parfor" />
			</xsl:when>
			<xsl:when test="@xsi:type = 'if'">
				<xsl:call-template name="if" />
			</xsl:when>							
			<xsl:otherwise>
				<xsl:message>unrecognized type of Activity:- '<xsl:value-of select="@xsi:type"/>'</xsl:message>
			</xsl:otherwise>														
		</xsl:choose>
</xsl:template>

<xsl:template match="wf:workflow" >
  <rules workflow-name="{@name}" rootState="{generate-id()}">
<!-- rules for entry and exit points into the interpreter -->
<rule trigger="_getStatus('{generate-id()}') == UNSTARTED" name="workflow-start">
	<action>
_setStatus('<xsl:value-of select="generate-id()"/>',STARTED)	
_setStatus('<xsl:value-of select="generate-id(./wf:sequence | ./wf:Activity[@xsi:type='sequence'])"/>',START)
	</action>
</rule>
<rule trigger="_getStatus('{generate-id()}') == STARTED and _getStatus('{generate-id(./wf:sequence | ./wf:Activity[@xsi:type='sequence'])}') == FINISHED" name="workflow-end">
	<action>
_setStatus('<xsl:value-of select="generate-id(.)"/>',FINISHED)
	</action>
</rule>
    <xsl:comment>Activity Rules</xsl:comment>
    <xsl:apply-templates select=".//wf:*" />
    <xsl:comment>Error handlers</xsl:comment>
    <xsl:apply-templates select=".//wf:*" mode="errors"/>
  </rules>
</xsl:template>

<!-- error handling - create rules that ensure errors get propagated upwards. -->
<xsl:template mode="errors" match="wf:workflow|wf:flow|wf:sequence|wf:script|wf:step|wf:while|wf:for|wf:if|wf:then|wf:else|wf:parfor|wf:Activity">
  <rule trigger="_getStatus('{generate-id()}') == ERROR" name="error-handler">
    <action>
_setStatus('<xsl:value-of select="generate-id(..)"/>',ERROR)
   </action>
  </rule>
</xsl:template>

<xsl:template match="wf:script" name="script">
<!-- set the statis to started, execute the script body, set status to finished
trigger is standard, env taken from associated state 
tested
-->
<xsl:comment>script</xsl:comment>
<rule trigger="_getStatus('{generate-id()}') == START" name="script" env="{generate-id()}">
	<action>
_setStatus('<xsl:value-of select="generate-id()"/>',STARTED)
	</action>
	<action>
	<xsl:value-of select="." />
	</action>
	<action>
_setStatus('<xsl:value-of select="generate-id()"/>',FINISHED)
	</action>
</rule>
</xsl:template>

<xsl:template match="wf:sequence" name="sequence">
<!-- rules for a sequence 
- statt: pass env into first child, call start on it.
- create a rule for each element in the sequence that chains one to the next, propagating environment
- end: when last child in the sequence finishes, the sequence finishes.
tested
-->
<xsl:comment>sequence</xsl:comment>
<rule trigger="_getStatus('{generate-id()}') == START" name="sequence-start">	
	<action>
_setStatus('<xsl:value-of select="generate-id(./*)"/>',START)
_setEnv('<xsl:value-of select="generate-id(./*)"/>',_getEnv('<xsl:value-of select="generate-id()"/>')) 
_setStatus('<xsl:value-of select="generate-id()"/>',STARTED)	  
	</action>	
</rule>
<xsl:for-each select="./*[not(position() = last())]">
<rule trigger="_getStatus('{generate-id()}') == FINISHED and _getStatus('{generate-id(./following-sibling::*)}') == UNSTARTED" name="sequence chain">
	<action>
_setStatus('<xsl:value-of select="generate-id(./following-sibling::*)"/>',START)
_setEnv('<xsl:value-of select="generate-id(./following-sibling::*)"/>',_getEnv('<xsl:value-of select="generate-id()"/>'))
	</action>
</rule>
</xsl:for-each>
<rule trigger="_getStatus('{generate-id()}') == STARTED and _getStatus('{generate-id(./*[position() = last()])}') == FINISHED" name="sequence-end">	
	<action>
_setStatus('<xsl:value-of select="generate-id()"/>', FINISHED) 
	</action>
</rule>
</xsl:template>

<xsl:template match="wf:flow" name="flow">
<!-- rules for a flow 
- start fires off all children at once, passing a clone of the environment to each
- finish waits for all children to complete, then merges any environment alterations.
- TODO - need to add in error-handling, join conditions here.
tested
-->
<xsl:comment>flow</xsl:comment>
<rule trigger="_getStatus('{generate-id()}') == START" name="flow-start">
	<xsl:for-each select="./*">
	<action>
_setStatus('<xsl:value-of select="generate-id()"/>',START)
_setEnv('<xsl:value-of select="generate-id()"/>',_cloneEnv(_getEnv('<xsl:value-of select="generate-id(..)"/>')))
	</action>	
	</xsl:for-each>
	<action>
_setStatus('<xsl:value-of select="generate-id()"/>',STARTED)
	</action>
</rule>
<!--problemo with with white space here - would like to lose it all -->
<rule name="flow-end">
	<xsl:attribute name="trigger">_getStatus('<xsl:value-of select="generate-id()"/>') == STARTED and <xsl:for-each select="./*"> <xsl:if test="position() != 1"> and </xsl:if>_getStatus('<xsl:value-of select="generate-id()"/>') == FINISHED</xsl:for-each></xsl:attribute>
	<action>
	envList = [
	<xsl:for-each select="./*">
	  <xsl:if test="position() != 1">,</xsl:if> '<xsl:value-of select="generate-id()"/>'
	</xsl:for-each>
	]
_setEnv('<xsl:value-of select="generate-id()"/>',_mergeEnvs(envList))
_setStatus('<xsl:value-of select="generate-id()"/>',FINISHED) 
	</action>
</rule>
</xsl:template>

<xsl:template match="wf:step" name="step">
<!-- rules for a step execution
start - get envronment from associated state, crate tool object, populate with parameters, call cea
finish - triggered by response from cea. collects any results from the call back into environment
TODO - implement this fully - just a dummy for now, while prototyping -->
<xsl:comment>step <xsl:value-of select="@name"/></xsl:comment>
<rule name="step-start" trigger="_getStatus('{generate-id()}') == START" env="{generate-id()}">
<action>
_setStatus('<xsl:value-of select="generate-id()"/>',STARTED)
_dispatchStep('<xsl:value-of select="generate-id()"/>')
</action>
</rule>
<rule name="step-end" trigger="_getStatus('{generate-id()}') == FINISH" env="{generate-id()}">
<action>
_setStatus('<xsl:value-of select="generate-id()"/>',FINISHED)
_completeStep('<xsl:value-of select="generate-id()"/>')
</action>
</rule>
</xsl:template>

<xsl:template match="wf:if" name="if">
<!-- rules for an if
evaluate the expression, bracnh accordingly. different behaviour depending on whether if has an else or not
tested
-->
<xsl:comment>if</xsl:comment>
<rule name="if-start" trigger="_getStatus('{generate-id()}') == START" env="{generate-id()}">
<action>
if <xsl:value-of select="@test" />:
	  	_setStatus('<xsl:value-of select="generate-id(./then/*)"/>', START)
		_setStatus('<xsl:value-of select="generate-id()"/>', STARTED)
		_setEnv('<xsl:value-of select="generate-id(./then/*)"/>',_getEnv('<xsl:value-of select="generate-id()"/>'))
else:
	  <xsl:choose>
	    <xsl:when test="./else">
	        _setStatus('<xsl:value-of select="generate-id(./else/*)"/>',START)
		_setStatus('<xsl:value-of select="generate-id()"/>',STARTED) 
		_setEnv('<xsl:value-of select="generate-id(./else/*)"/>',_getEnv('<xsl:value-of select="generate-id()"/>'))		
	    </xsl:when>
	    <xsl:otherwise>
	        _setStatus('<xsl:value-of select="generate-id()"/>',FINISHED)
	    </xsl:otherwise>
	  </xsl:choose>
</action>
</rule>
<rule name="if-end">
	<xsl:attribute name="trigger">
	  <xsl:choose>
	    <xsl:when test="./else">_getStatus('<xsl:value-of select="generate-id()"/>') == STARTED and (_getStatus('<xsl:value-of select="generate-id(./then/*)"/>') == FINISHED or _getStatus('<xsl:value-of select="generate-id(./else/*)"/>') == FINISHED)</xsl:when>
	    <xsl:otherwise>_getStatus('<xsl:value-of select="generate-id()"/>') == STARTED and _getStatus('<xsl:value-of select="generate-id(./then/*)"/>') == FINISHED</xsl:otherwise>
	  </xsl:choose>
	</xsl:attribute>
	<action>
_setStatus('<xsl:value-of select="generate-id()"/>',FINISHED)
	</action>
</rule>
</xsl:template>

<xsl:template match="wf:while" name="while">
<!-- rules for an while
to start - if test passes, trigger body rule, pass current environment in.
to continue - if test passes, re-trigger body rule. 
tested
-->
<xsl:comment>while</xsl:comment>
<rule name="while-init" trigger="_getStatus('{generate-id()}') == START" env="{generate-id()}">
	<action>
if <xsl:value-of select="@test" />:
		_setStatus('<xsl:value-of select="generate-id(./*)"/>',START)
	        _setStatus('<xsl:value-of select="generate-id()"/>',STARTED) 
		_setEnv('<xsl:value-of select="generate-id(./*)"/>',_getEnv('<xsl:value-of select="generate-id()"/>'))
else:
		_setStatus('<xsl:value-of select="generate-id()"/>', FINISHED)
	</action>
</rule>
<rule name="while-loop" 
	trigger="_getStatus('{generate-id()}') == STARTED and _getStatus('{generate-id(./*)}') == FINISHED" env="{generate-id()}">
	<action>
if <xsl:value-of select="@test" />:
		_setStatus('<xsl:value-of select="generate-id(./*)"/>',START)
else:
		_setStatus('<xsl:value-of select="generate-id()"/>', FINISHED)
	</action>
</rule>
</xsl:template>

<xsl:template match="wf:for" name="for">
<xsl:comment>for</xsl:comment>
<!-- rules for a for - similar to while. 
unsure whether the environment should be copied back out of a for loop. should we have child environments here instead.
hmmm - subtle. further work
-->
<rule name="for-init" trigger="_getStatus('{generate-id()}') == START" env="{generate-id()}">
	<action>
_<xsl:value-of select="generate-id()"/>_iter = iter(<xsl:value-of select="@range" />)
_setStatus('<xsl:value-of select="generate-id()"/>',STARTED)
try:
		<xsl:value-of select="@var" /> = _<xsl:value-of select="generate-id()"/>_iter.next()
		_setStatus('<xsl:value-of select="generate-id(./*)" />',START)
		_setEnv('<xsl:value-of select="generate-id(./*)"/>',_getEnv('<xsl:value-of select="generate-id()"/>'))		
except:
		_setStatus('<xsl:value-of select="generate-id()"/>',FINISHED)		
	</action>
</rule>
<rule name="for-loop" trigger="_getStatus('{generate-id()}') == STARTED and _getStatus('{generate-id(./*)}') == FINISHED" env="{generate-id()}">
	<action>
try:
		<xsl:value-of select="@var" /> = _<xsl:value-of select="generate-id()"/>_iter.next()
		_setStatus('<xsl:value-of select="generate-id(./*)" />',START)
except:
		_setStatus('<xsl:value-of select="generate-id()"/>',FINISHED)	
	</action>
</rule>
</xsl:template>

<xsl:template match="wf:parfor" name="parfor">
<!-- Phew! - this is the biggie. all the rest of the language constructs are static in some way.
even the for and while loops - they repeat the specified steps a number of times, but do not create new steps - so we can
use generate-id() to refer to things cleanly.

parfor is different - it spawns off multiple concurrent copies of the same subtree of the worflow - so we can't use
generate-id() to reference things anymore - as such an id will refer to a step in each of the concurrent branches.

approcach I'm taking is to dynamically grow the (compiled representation of) the tree - i.e. at runtime, where we know the 
number of concurrent copies there's going to be, this rule adds more rules to the store. the generated rules implement each of the branches.

confused? oh yes :)
issues - what happens when parfor is itself in a sequential loop - don't need to code generate twice.
-->
<xsl:comment>parfor</xsl:comment>
<rule name="parfor-start" trigger="_getStatus('{generate-id()}') == START">
	<action>
_<xsl:value-of select="generate-id()"/>_range = <xsl:value-of select="@range" />
#list of ids of states in the par-for subtree - used as a template.
_<xsl:value-of select="generate-id()"/>_stateList = [
		 <xsl:for-each select=".//flow|.//sequence|.//script|.//step|.//while|.//for|.//if|.//then|.//else|.//Activity">
		 <xsl:if test="position() != 1">,</xsl:if>'<xsl:value-of select="generate-id()"/>'
		 </xsl:for-each>
		  ]
		  
_<xsl:value-of select="generate-id()"/>_branchRoot = lambda i: '<xsl:value-of select="generate-id(./*)"/>:' + str(i)
		  
#for each branch, create a new set of rules using the rules for the above states as templates.		  
for (ix,val) in zip(range(len(_<xsl:value-of select="generate-id()"/>_range)),_<xsl:value-of select="generate-id()"/>_range):
		_addParallelBranch(ix,"<xsl:value-of select="@var"/>",val,_<xsl:value-of select="generate-id()"/>_stateList)
		#now trigger root of branch.
		_setStatus(_<xsl:value-of select="generate-id()"/>_branchRoot(ix),START)
		env = _cloneEnv(_getEnv('<xsl:value-of select="generate-id()"/>'))
		env["<xsl:value-of select="@var"/>"] = val # add new binding
		_setEnv(_<xsl:value-of select="generate-id()"/>_branchRoot(ix),env)
_addEndRule('<xsl:value-of select="generate-id()"/>',_<xsl:value-of select="generate-id()"/>_branchRoot,len(_<xsl:value-of select="generate-id()"/>_range))
# created all dynamic rules, so set this one to started.
setStatus('<xsl:value-of select="generate-id()"/>',STARTED)
	</action>
</rule>
</xsl:template>


</xsl:stylesheet>

