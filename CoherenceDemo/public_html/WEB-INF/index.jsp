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
    <div id="wrap">
    <div>
    <div id="header">
    <table width="100%">
    <tr><td align="left"> <img src="images/CoherenceImg.png"/></td>
    <td align="right"> <img src="images/AcmeCommunications_logo.gif"/></td></tr></table>
       
        
      </div>
    </div>
      
     <fieldset>
      <legend>
        Customer Self Service Portal -
        <%= siteLocation %>
      </legend>
      <span>
        ${status}</span>
      <div class="CSSTableGenerator">
        <table>
          <tr>
            <td>Feature</td>
            <td>Service Description</td>
          </tr>
           
          <tr>
            <td>
              <a href="coherencedemo?requestType=initial">
                <button>Create Customer</button></a>
            </td>
            <td>Service Description</td>
          </tr>
           
          <tr>
            <td>
              <a href="coherencedemo?requestType=search">
                <button>Search Customer</button></a>
            </td>
            <td>Service Description</td>
          </tr>
        </table>
      </div>
    </fieldset>
      <div id="footer">
        <p>
          <a href="coherencedemo?requestType=login">Home</a>
           &copy; 2014 Copyright Oracle
        </p>
      </div>
    </div>
  </body>
</html>