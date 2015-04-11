<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
  <head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"></meta>
    <title>Customer Self Service Portal</title>
    <link rel="stylesheet" type="text/css" href="css/style.css"></link>
  </head>
  <body>
    <%
  String siteLocation = "";
  try {
  siteLocation = "";
  com.oracle.datagrid.activeactive.Coherence c = new com.oracle.datagrid.activeactive.Coherence();
  siteLocation = c.getClusterLocation();
  } catch (Exception e) {
  System.out.println(e);
  }
  
  
  %>
    <div id="loginwrap">
      <div>
        <div id="header">
          <table width="100%">
            <tr>
              <td align="center">
                <img src="images/AcmeCommunications_logo.gif"/>
              </td>
            </tr>
          </table>
        </div>
      </div>
      <fieldset>
        <legend>
          <%= siteLocation %>
          - Grid Data Center Login
        </legend>
        <span>
          ${status}</span>
        <div class="CSSTableGenerator">
        <form method="POST" action="coherencedemo?requestType=login">
          <table>
            <tr>
              <td>Login</td>
            </tr>
             
            <tr>
              <td>
                <p>
                  <label for="login" class="field">Login</label>
                   
                  <input type="text" name="login" id="login" required></input>
                </p>
                <p>
                  <label for="password" class="field">Password</label>
                   
                  <input type="password" name="password" id="password" required></input>
                </p>
                 <p>
                   <input type="Submit" src="index.jsp" value="Login">
                </p>
              </td>
            </tr>
            
          </table>
          </form>
        </div>
      </fieldset>
      <div id="footer">
        <p>&copy; 2014 Copyright Oracle</p>
      </div>
    </div>
  </body>
</html>