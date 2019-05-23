<!DOCTYPE html>
<!Zachary Pell>

<html>
    <head>
        <title>Search</title>
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
            <br>
        </form>
        <div>
            <form method="POST" action="Controller">
                <p>Name of Weapon<br><input type="text" name="name">
                
                <p><input class="blue" type="submit" name="goButton" value="GO">
            </form>
        </div>
    </body>
</html>
