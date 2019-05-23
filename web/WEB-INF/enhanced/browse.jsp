<!DOCTYPE html>
<!Zachary Pell>

<html>
    <head>
        <title>Weapons</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core" %>
        <link REL="stylesheet" TYPE="text/css" HREF="project.css">
    </head>
    <body>
        <h1>Fortnite Database</h1>
        
        <form method="GET" action="Controller">
            <p class="center">
                <input class="green" type="submit" name="browseButton" value="Browse Weapons">
                <input class="blue" type="submit" name="newButton" value="New Weapon">
                <input class="purple" type="submit" name="searchButton" value="Search">
            </p>
        </form>
        <br>
        <table>
            <th>Name	
            <th>Rarity	
            <th>DPS	
            <th>Damage	
            <th>Env Damage	
            <th>Fire rate	
            <th>Magazine	
            <th>Reload time
            <core:forEach var="row" items="${database}">
                <tr>
                    <td>${row.name}
                    <td>${row.rarity}
                    <td>${row.dps}
                    <td>${row.damage}
                    <td>${row.env}
                    <td>${row.rate}
                    <td>${row.mag}
                    <td>${row.reload}
                </tr>
            </core:forEach>
        </table>
    </body>
</html>
