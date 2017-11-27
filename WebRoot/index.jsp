<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
        <link rel="stylesheet" type="text/css" href="styles/login.css"/>
        <title>iOSTS</title>	
	</head>
	<body>

        <div id="login-ui" style="display: none">
            <div id="login-dialog-middle">

                <div id="login-dialog">

                    <p id="login-error"></p>

                    <form id="login-form" action="#" method="post">

                        <div id="login-fields">
							<% 
							String username = request.getParameter("username");
							String id = request.getParameter("id");
							String server = request.getParameter("server");
							String serialNumber = request.getParameter("serialNumber");
							String fromUrl = request.getParameter("fromUrl");
							String lefttime = request.getParameter("lefttime");
							%>
                            <table>
                                <tr>
                                    <th>Username</th>
                                    <td><input type="text" name="username" id="username" autofocus="autofocus" value="<%=serialNumber%>"/></td>
                                </tr>
                                <tr>
                                    <th>Password</th>
                                    <td><input type="text" name="password" id="password" value="molab"/></td>
                                </tr>
                            </table>							
							
                            <img class="logo" src="images/guacamole-logo-64.png" alt=""/>
                        </div>

                        <div id="buttons">
                            <input type="submit" name="login" id="login" value="Login"/>
                        </div>

                    </form>
                </div>

            </div>
        </div>

        <!-- Connection list UI -->
        <div id="connection-list-ui" style="display: none">

            <div id="logout-panel">
                <button id="logout">Logout</button>
            </div>
            
            <h1>
                <img class="logo" src="images/guacamole-logo-64.png" alt=""/>
                Available Connections
            </h1>
            
            <table class="connections">
                <thead>
                    <tr>
                        <th class="protocol"> </th>
                        <th class="name">Name</th>
                    </tr>
                </thead>
                <tbody id="connections-tbody">
                </tbody>
            </table>

        </div>

        <div id="version-dialog">
            Guacamole 0.5.0
        </div>

        <script type="text/javascript" src="scripts/connections.js"></script>

        <!-- Init -->
        <script type="text/javascript"> /* <![CDATA[ */

            function resetUI() {

                var configs;
                try {
                    configs = getConfigList();
                }
                catch (e) {

                    // Show login UI if unable to get configs
                    loginUI.style.display = "";
                    connectionListUI.style.display = "none";

                    return;

                }

                // If only one connection, redirect to that.
                if (configs.length == 1) {                    
                    window.location.href = "client.jsp?" + encodeURIComponent(configs[0].id) + "&username=<%=username%>&id=<%=id%>&server=<%=server%>&serialNumber=<%=serialNumber%>&fromUrl=<%=fromUrl%>&lefttime=<%=lefttime%>";
                    return;
                }

                // Remove all rows from connections list
                var tbody = document.getElementById("connections-tbody");
                tbody.innerHTML = "";
                
                // Add one row per connection
                for (var i=0; i<configs.length; i++) {

                    // Create row and cells
                    var tr = document.createElement("tr");
                    var protocol = document.createElement("td");
                    var id = document.createElement("td");

                    var protocolIcon = document.createElement("div");
                    protocolIcon.className = "protocol icon " + configs[i].protocol;

                    // Set CSS
                    protocol.className = "protocol";
                    id.className = "name";

                    // Create link to client
                    var clientLink = document.createElement("a");                    
                    clientLink.setAttribute("href",
                        "client.jsp?" + encodeURIComponent(configs[i].id)) + "&username=<%=username%>&id=<%=id%>&server=<%=server%>&serialNumber=<%=serialNumber%>&fromUrl=<%=fromUrl%>&lefttime=<%=lefttime%>";

                    // Set cell contents
                    protocol.appendChild(protocolIcon);
                    //protocol.textContent   = configs[i].protocol;
                    clientLink.textContent = configs[i].id;
                    id.appendChild(clientLink);

                    // Add cells
                    tr.appendChild(protocol);
                    tr.appendChild(id);

                    // Add row
                    tbody.appendChild(tr);

                }

                // If configs could be retrieved, display list
                loginUI.style.display = "none";
                connectionListUI.style.display = "";

            }

            var loginForm = document.getElementById("login-form");
            var loginUI = document.getElementById("login-ui");
            var connectionListUI = document.getElementById("connection-list-ui");
            var logout = document.getElementById("logout");

            logout.onclick = function() {
                window.location.href = "logout";
            };

            // TODO: Get connection list
            // On no-auth fail, show login UI 

            loginForm.onsubmit = function() {

                var username = document.getElementById("username");
            //    var password = document.getElementById("password");
            	var password = "molab";

                var data =
                       "username=" + encodeURIComponent(username.value)
                    + "&password=molab"// + encodeURIComponent(password.value)

                try {

                    // Log in
                    var xhr = new XMLHttpRequest();
                    xhr.open("POST", "login", false);
                    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                    xhr.send(data);

                    // Handle failures
                    if (xhr.status != 200)
                        throw new Error("系统异常，请联系管理员!");

                    resetUI();

                }
                catch (e) {

                    var loginError = document.getElementById("login-error");

                    // Display error, reset and refocus password field
                    loginError.textContent = e.message;
                    password.value = "";
                    password.focus();

                    return false;

                }

                // On success, hide loginUI, get and show connection list.
                return false;

            }

            resetUI();
            
            document.getElementById("login").click();

            /* ]]> */ </script>	
            
	</body>
</html>
