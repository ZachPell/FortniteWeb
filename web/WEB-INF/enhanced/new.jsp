<!DOCTYPE html>
<!Zachary Pell>

<html>
    <head>
        <title>New Weapon</title>
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
        <div>
            <form method="POST" action="Controller">
                <p>Name<br><input type="text" name="name">
                <p>Rarity<br><input type="text" name="rarity">
                <p>DPS<br><input type="text" name="dps">
                <p>Damage<br><input type="text" name="damage">
                <p>Env Damage<br><input type="text" name="env">
                <p>Fire Rate<br><input type="text" name="rate">
                <p>Magazine<br><input type="text" name="mag">
                <p>Reload Time<br><input type="text" name="reload">

                <p><input class="gold" type="submit" name="submitButton" value="submit">
            </form>
        </div>
    </body>
</html>
