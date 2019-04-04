<#list versions as version>
    <!-- ${version.description}<br> -->
    <#assign tickets = tickets[version]/>
    <#list tickets as ticket>
        ${ticket.tracker} <a href="https://pm.lksoft.lt/issues/${ticket.id?c}">#${ticket.id?c}</a>: ${ticket.subject}<br>
    </#list>
</#list>
