<#import "common/common.ftl" as c>
<#import "common/login.ftl" as l>

<@c.page>
    Login page
    <@l.login "/login"/>
    <a href = "/registration">Add new user</a>
</@c.page>