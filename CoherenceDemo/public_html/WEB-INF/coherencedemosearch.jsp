<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
  <head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <title>Customer Self Service Portal</title>
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    
  </head>
  <body>
  <form name="demo" id="demo" action="coherencedemo?requestType=search" method="POST">
    <div id="wrap">
      <div id="header">
        <img src="images/CoherenceImg.png"/>
      </div>
      <fieldset>
        <legend>Search Customer</legend>
        <div id="search">
          
              <label for="lname" class="field">Last Name</label>
               
              <input type="text" name="lname" id="lname" required="required">
              <input type="Submit" value="Search"/>
         
          <div class="CSSTableGenerator">
            <table width="100%">
              <tr>
                <td>ID</td>
                <td>Name</td>
                <td>Address</td>
                <td>State</td>
                <td>City</td>
                <td>Country</td>
                <td>Telephone</td>
              </tr>
               
              <c:forEach var="customer" items="${searchresults}">
                <tr>
                  <td>
                    <a href="coherencedemo?requestType=updateRequest&customerID=${customer.customerId}">
                      ${customer.customerId}</a>
                  </td>
                  <td>
                    ${customer.customerName.firstName}
                     
                    ${customer.customerName.lastName}
                  </td>
                  <td>
                    ${customer.customerLocation.adress}
                  </td>
                  <td>
                    ${customer.customerLocation.state}
                  </td>
                  <td>
                    ${customer.customerLocation.city}
                  </td>
                  <td>
                    ${customer.customerLocation.country}
                  </td>
                  <td>
                    ${customer.customerPhone}
                  </td>
                </tr>
              </c:forEach>
            </table>
          </div>
        </div>
        <div id="sidebar"></div>
      </fieldset>
      <div id="footer">
        <p>
          <a href="coherencedemo?requestType=login">Home</a>
           &copy; 2014 Copyright Oracle
        </p>
      </div>
    </div>
     </form>
  </body>
</html>