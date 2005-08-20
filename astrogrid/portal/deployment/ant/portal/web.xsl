<?xml version="1.0" encoding="UTF-8"?>
<stylesheet xmlns="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <template match="env-entry[env-entry-name='emailserver.address']/env-entry-value/text()">@SMTPSERVER@</template>
    <template match="env-entry[env-entry-name='emailserver.user']/env-entry-value/text()">@SMTPUSER@</template>
    <template match="env-entry[env-entry-name='emailserver.password']/env-entry-value/text()">@SMTPPASS@</template>
    <template match="env-entry[env-entry-name='emailserver.from']/env-entry-value/text()">@MAILFROM@</template>
    <template match="env-entry[env-entry-name='astrogrid.portal.admin.email']/env-entry-value/text()">@ADMINEMAIL@</template>
    <template match="env-entry[env-entry-name='org.astrogrid.registry.query.endpoint']/env-entry-value/text()">@CENTRALREGISTRYQUERY@</template>
    <template match="env-entry[env-entry-name='org.astrogrid.registry.admin.endpoint']/env-entry-value/text()">@CENTRALREGISTRYUPDATE@</template>
    <template match="env-entry[env-entry-name='workflow.jes.endpoint']/env-entry-value/text()">@JOBENTRYSYSTEM@/services/JobControllerService</template>
    <template match="env-entry[env-entry-name='workflow.applist']/env-entry-value/text()">registry</template>
    <template match="@*|node()">
        <copy>
            <apply-templates select="@*|node()"/>
        </copy>
    </template>
</stylesheet>


