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
<rule>
  <trigger>boolean-expression</trigger>
  <name>textual name</name>
  <envId>key"</envId>
  <body>
    script statements
  </body>
</rule>

the trigger element (required) should be a boolean expression with no side effects - as its going to be 
evaluated frequently. when it evaluates to true, the rule is able to fire.

the name element (optional) of the rule is used for documentation and logging

the env element (optional) gives a key to a state. the environment stored in this state
is the environment the body of the rule should be executed in. if no env is declared, then
the body of the rule is executed in the default environment

the body element (required) contains script instructions.
they are executed in order by the script interpreter.

the script interpreter is required to provide the following methods in the default environment
state.setStatus - set status of a state
state.getStatus - get status of a state.
state.getEnv - get the environment from a state
state.setEnv - set the environment for a state.
cloneEnvs - produce a shallow copy of an environment
vars.mergeEnvs - merge a list of environments into a single environment
and the constants UNSTARTED, START, STARTED, FINISH, FINISHED, ERROR
Finally, the implementation of parfor requires two further methods, to add dynamically-generated rules into the rulebase.
rules.addParallelBranch
rules.addEndRule

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
			<xsl:when test="@xsi:type = 'set'">
				<xsl:call-template name="set" />
			</xsl:when>	
			<xsl:when test="@xsi:type = 'unset'">
				<xsl:call-template name="unset" />
			</xsl:when>	
			<xsl:when test="@xsi:type = 'scope'">
				<xsl:call-template name="scope" />
			</xsl:when>									
			<xsl:when test="@xsi:type = 'try'">
				<xsl:call-template name="try" />
			</xsl:when>	
			<xsl:when test="@xsi:type = 'catch'">
				<xsl:call-template name="catch" />
			</xsl:when>								
			<xsl:otherwise>
				<xsl:message>unrecognized type of Activity:- '<xsl:value-of select="@xsi:type"/>'</xsl:message>
			</xsl:otherwise>														
		</xsl:choose>
</xsl:template>

<xsl:template match="wf:workflow" >
	
  <rules>
	<name><xsl:value-of select="@name"/></name> 
	<rootState><xsl:value-of select="generate-id()"/></rootState>
	 <rules>
<!-- rules for entry and exit points into the interpreter -->
<rule>
	 <trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == UNSTARTED</trigger>
		<name>workflow-start</name>
	<body>
states.setStatus('<xsl:value-of select="generate-id()"/>',STARTED);
jes.setWorkflowStatus(STARTED);
states.setStatus('<xsl:value-of select="generate-id(./wf:sequence | ./wf:Activity[@xsi:type='sequence'])"/>',START);
	</body>
</rule>

<rule> 
	<trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == STARTED &amp;&amp; states.getStatus('<xsl:value-of select="generate-id(./wf:sequence | ./wf:Activity[@xsi:type='sequence'])"/>') == FINISHED</trigger>
	<name>workflow-end</name>
	<body>
states.setStatus('<xsl:value-of select="generate-id()"/>',FINISHED);
jes.setWorkflowStatus(FINISHED);
	</body>
</rule>

<rule>
	<trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == ERROR</trigger>
	<name>workflow-error</name>
	<body>
		states.setStatus('<xsl:value-of select="generate-id()"/>',ERRED);
		jes.setWorkflowStatus(ERROR);
	</body>
</rule>
    <xsl:comment>Activity Rules</xsl:comment>
    <xsl:apply-templates select=".//wf:*" />
    <xsl:comment>Error handlers</xsl:comment>
    <xsl:apply-templates select=".//wf:*" mode="errors"/>
	</rules>
  </rules>
</xsl:template>

<!-- error handling - create rules that ensure errors get propagated upwards. -->
<xsl:template mode="errors" match="wf:set|wf:scope|wf:unset|wf:catch|wf:try|wf:flow|wf:sequence|wf:script|wf:step|wf:while|wf:for|wf:if|wf:then|wf:else|wf:parfor|wf:Activity">
  <rule>
	<trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == ERROR</trigger>
	<name>error-handler</name>
    <body>
states.setStatus('<xsl:value-of select="generate-id()"/>',ERRED);
states.setStatus('<xsl:value-of select="generate-id(..)"/>',ERROR);
   </body>
  </rule>
</xsl:template>

<!-- rules for variables -->
<xsl:template match="wf:set" name="set">
	<rule>
		<trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == START</trigger>
		<name>set-variable</name>
		<envId><xsl:value-of select="generate-id()"/></envId>
		<body>
			if (jes.executeSet('<xsl:value-of select="generate-id()"/>',shell,states,rules)) {
				states.setStatus('<xsl:value-of select="generate-id()"/>',FINISHED);
			} else {
				states.setStatus('<xsl:value-of select="generate-id()"/>',ERROR);
			}
		</body>
		</rule>
</xsl:template>
	
<xsl:template match="wf:unset" name="unset">
	<rule>
		<trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == START</trigger>
		<name>unset-variable</name>
		<body>
			states.getEnv('<xsl:value-of select="generate-id()"/>').unset('<xsl:value-of select="@var"/>');
			states.setStatus('<xsl:value-of select="generate-id()"/>',FINISHED);
		</body>
	</rule>
</xsl:template>

<xsl:template match="wf:scope" name="scope">
	<rule>
		<trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == START</trigger>
		<name>introduce-scope</name>
		<body>
			e = states.getEnv('<xsl:value-of select="generate-id()"/>');
			e.newScope();
			states.setStatus('<xsl:value-of select="generate-id()"/>',STARTED);
			states.setStatus('<xsl:value-of select="generate-id(./*)"/>',START);
			states.setEnv('<xsl:value-of select="generate-id(./*)"/>',e);
		</body>
	</rule>
	<rule>
		<trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == STARTED &amp;&amp; states.getStatus('<xsl:value-of select="generate-id(./*)"/>') == FINISHED</trigger>
		<name>remove-scope</name>
		<body>
			states.getEnv('<xsl:value-of select="generate-id()"/>').removeScope();
			states.setStatus('<xsl:value-of select="generate-id()"/>',FINISHED);
		</body>
	</rule>			
</xsl:template>

<!-- atomic activities -->
<xsl:template match="wf:script" name="script">
<!-- set the statis to started, execute the script body, set status to finished
trigger is standard, env taken from associated state 
tested
-->
<xsl:comment>script</xsl:comment>
<rule> 
	<trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == START</trigger>
	<name>script</name>
	<envId><xsl:value-of select="generate-id()"/></envId>
	<body>
states.setStatus('<xsl:value-of select="generate-id()"/>',STARTED);
if(jes.dispatchScript('<xsl:value-of select="generate-id()"/>',shell,states,rules)) {
states.setStatus('<xsl:value-of select="generate-id()"/>',FINISHED);
} else {
  states.setStatus('<xsl:value-of select="generate-id()"/>',ERROR);
}
	</body>
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
<rule>
	<trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == START</trigger>
	 <name>sequence-start</name>
	<body>
<xsl:if test="./*"><!-- i.e. we've got children -->
states.setStatus('<xsl:value-of select="generate-id(./*)"/>',START);
states.setEnv('<xsl:value-of select="generate-id(./*)"/>',states.getEnv('<xsl:value-of select="generate-id()"/>')) ;
</xsl:if>
states.setStatus('<xsl:value-of select="generate-id()"/>',STARTED)	  
	</body>	
</rule>

<xsl:for-each select="./*[not(position() = last())]">
<rule>
	 <trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == FINISHED &amp;&amp; states.getStatus('<xsl:value-of select="generate-id(./following-sibling::*)"/>') == UNSTARTED</trigger>
	 <name>sequence chain</name>
	<body>
states.setStatus('<xsl:value-of select="generate-id(./following-sibling::*)"/>',START);
states.setEnv('<xsl:value-of select="generate-id(./following-sibling::*)"/>',states.getEnv('<xsl:value-of select="generate-id()"/>'));
	</body>
</rule>
</xsl:for-each>

<rule>
	<trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == STARTED 
		<xsl:if test="./*"><!-- we've got children -->
		&amp;&amp; states.getStatus('<xsl:value-of select="generate-id(./*[position() = last()])"/>') == FINISHED
		</xsl:if></trigger>
	<name>sequence-end</name>
	<body>
states.setStatus('<xsl:value-of select="generate-id()"/>', FINISHED) ;

	</body>
</rule>
</xsl:template>

<xsl:template match="wf:flow" name="flow">
<!-- rules for a flow 
- start fires off all children at once, passing a clone of the environment to each
- finish waits for all children to complete, then merges any environment alterations.
- TODO - need to add in error-handling,
- ALTERATION - no manipulation of environment. all share same. can use &lt;scope&gt; tag explicitly if needed.
-->
<xsl:comment>flow</xsl:comment>
<rule>
	 <trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == START</trigger>
	 <name>flow-start</name>
	 <body>
	<xsl:for-each select="./*">
states.setStatus('<xsl:value-of select="generate-id()"/>',START);
states.setEnv('<xsl:value-of select="generate-id()"/>',states.getEnv('<xsl:value-of select="generate-id(..)"/>'));
<!--.cloneVars());-->
	</xsl:for-each>
states.setStatus('<xsl:value-of select="generate-id()"/>',STARTED);
	</body>
</rule>
<rule>
	<name>flow-end</name>
	<trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == STARTED 
			<xsl:for-each select="./*"> 
				&amp;&amp; states.getStatus('<xsl:value-of select="generate-id()"/>') == FINISHED
		    </xsl:for-each>
	</trigger>
	<body>
	<!-- not needed now
	envList = [
	<xsl:for-each select="./*">
	  <xsl:if test="position() != 1">,</xsl:if> '<xsl:value-of select="generate-id()"/>'
	</xsl:for-each>
	];
states.setEnv('<xsl:value-of select="generate-id()"/>',states.mergeVars(envList));
-->
states.setStatus('<xsl:value-of select="generate-id()"/>',FINISHED) ;
	</body>
</rule>
</xsl:template>

<xsl:template match="wf:step" name="step">
<!-- rules for a step execution
start - get envronment from associated state, crate tool object, populate with parameters, call cea
finish - triggered by response from cea. collects any results from the call back into environment
TODO - implement this fully - just a dummy for now, while prototyping -->
<xsl:comment>step <xsl:value-of select="@name"/></xsl:comment>
<rule>
	<name>step-start</name>
	<trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == START</trigger>
    <envId><xsl:value-of select="generate-id()"/></envId>
<body>
states.setStatus('<xsl:value-of select="generate-id()"/>',STARTED);
jes.dispatchStep('<xsl:value-of select="generate-id()"/>');
</body>
</rule>

<rule> 
	<name>step-end</name>
	<trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == FINISH</trigger>
	<envId><xsl:value-of select="generate-id()"/></envId>
<body>
states.setStatus('<xsl:value-of select="generate-id()"/>',FINISHED);
jes.completeStep('<xsl:value-of select="generate-id()"/>');
</body>
</rule>
</xsl:template>

<xsl:template match="wf:if" name="if">
<!-- rules for an if
evaluate the expression, bracnh accordingly. different behaviour depending on whether if has an else or not
tested
-->
<xsl:comment>if</xsl:comment>
<rule>
	 <name>if-start</name>
	 <trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == START</trigger>
	 <envId><xsl:value-of select="generate-id()"/></envId>
<body>
if (<xsl:value-of select="@test" />) {
	  	states.setStatus('<xsl:value-of select="generate-id(./then/*)"/>', START);
		states.setStatus('<xsl:value-of select="generate-id()"/>', STARTED);
		states.setEnv('<xsl:value-of select="generate-id(./then/*)"/>',states.getEnv('<xsl:value-of select="generate-id()"/>'));
} else {
	  <xsl:choose>
	    <xsl:when test="./else">
	     states.setStatus('<xsl:value-of select="generate-id(./else/*)"/>',START);
		states.setStatus('<xsl:value-of select="generate-id()"/>',STARTED) ;
		states.setEnv('<xsl:value-of select="generate-id(./else/*)"/>',states.getEnv('<xsl:value-of select="generate-id()"/>'));
	    </xsl:when>
	    <xsl:otherwise>
	        states.setStatus('<xsl:value-of select="generate-id()"/>',FINISHED);
	    </xsl:otherwise>
	  </xsl:choose>
	  }
</body>
</rule>
<rule>
	<name>if-end</name>
	<trigger>
	  <xsl:choose>
	    <xsl:when test="./else">states.getStatus('<xsl:value-of select="generate-id()"/>') == STARTED &amp;&amp; (states.getStatus('<xsl:value-of select="generate-id(./then/*)"/>') == FINISHED || states.getStatus('<xsl:value-of select="generate-id(./else/*)"/>') == FINISHED)</xsl:when>
	    <xsl:otherwise>states.getStatus('<xsl:value-of select="generate-id()"/>') == STARTED &amp;&amp; states.getStatus('<xsl:value-of select="generate-id(./then/*)"/>') == FINISHED</xsl:otherwise>
	  </xsl:choose>
	</trigger>
	<body>
states.setStatus('<xsl:value-of select="generate-id()"/>',FINISHED);
	</body>
</rule>
</xsl:template>

<xsl:template match="wf:while" name="while">
<!-- rules for an while
to start - if test passes, trigger body rule, pass current environment in.
to continue - if test passes, re-trigger body rule. 
tested
-->
<xsl:comment>while</xsl:comment>
<rule>
	 <name>while-init</name>
	 <trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == START</trigger>
	 <envId><xsl:value-of select="generate-id()"/></envId>
	<body>
if (<xsl:value-of select="@test" />) {
		states.setStatus('<xsl:value-of select="generate-id(./*)"/>',START);
	       states.setStatus('<xsl:value-of select="generate-id()"/>',STARTED) ;
		states.setEnv('<xsl:value-of select="generate-id(./*)"/>',states.getEnv('<xsl:value-of select="generate-id()"/>'));
} else {
		states.setStatus('<xsl:value-of select="generate-id()"/>', FINISHED);
}
	</body>
</rule>
<rule>
	 <name>while-loop</name>
	<trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == STARTED &amp;&amp; states.getStatus('<xsl:value-of select="generate-id(./*)"/>') == FINISHED</trigger>
	<envId><xsl:value-of select="generate-id()"/></envId>
	<body>
if (<xsl:value-of select="@test" />) {
		states.setStatus('<xsl:value-of select="generate-id(./*)"/>',START);
} else {
		states.setStatus('<xsl:value-of select="generate-id()"/>', FINISHED);
}
	</body>
</rule>
</xsl:template>

<xsl:template match="wf:for" name="for">
<xsl:comment>for</xsl:comment>
<!-- rules for a for - similar to while. 
unsure whether the environment should be copied back out of a for loop. should we have child environments here instead.
hmmm - subtle. further work
@todo translate this body
-->
<rule>
	 <name>for-init</name>
	 <trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == START</trigger> 
	 <envId><xsl:value-of select="generate-id()"/></envId>
	<body>
_<xsl:value-of select="generate-id()"/>_iter = iter(<xsl:value-of select="@range" />)
_setStatus('<xsl:value-of select="generate-id()"/>',STARTED)
try:
		<xsl:value-of select="@var" /> = _<xsl:value-of select="generate-id()"/>_iter.next()
		_setStatus('<xsl:value-of select="generate-id(./*)" />',START)
		_setEnv('<xsl:value-of select="generate-id(./*)"/>',_getEnv('<xsl:value-of select="generate-id()"/>'))		
except:
		_setStatus('<xsl:value-of select="generate-id()"/>',FINISHED)		
	</body>
</rule>

<rule>
	 <name>for-loop</name>
	 <trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == STARTED &amp;&amp; states.getStatus('<xsl:value-of select="generate-id(./*)"/>') == FINISHED</trigger> 
	 <envId><xsl:value-of select="generate-id()"/></envId>
	<body>
try:
		<xsl:value-of select="@var" /> = _<xsl:value-of select="generate-id()"/>_iter.next()
		_setStatus('<xsl:value-of select="generate-id(./*)" />',START)
except:
		_setStatus('<xsl:value-of select="generate-id()"/>',FINISHED)	
	</body>
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
@todo translate this still.
-->
<xsl:comment>parfor</xsl:comment>
<rule>
	<name>parfor-start</name>
	<trigger>states.getStatus('<xsl:value-of select="generate-id()"/>') == START"</trigger>
	<body>
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
	</body>
</rule>
</xsl:template>


</xsl:stylesheet>

