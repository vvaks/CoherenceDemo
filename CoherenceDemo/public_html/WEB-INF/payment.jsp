<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
        <title>Customer Self Service Portal - Payment</title>
        <link rel="stylesheet" type="text/css" href="css/style.css">
        <script>
            var xmlHttp = new XMLHttpRequest();
            
            function purchaseService(){
                var accountNo = document.getElementById["accountNo"];;
                var serviceId = document.getElementById["serviceId"];;
                var ccNo = document.getElementById("ccNo");
                var fname = document.getElementById("fname");
                var lname = document.getElementById("lname");
                var expire = document.getElementById("expire");
                var provider = document.getElementById("provider");
                
                var urlEndpoint = "http://fmw01:7103/ServiceBuyFlowRESTProxy/?serviceId="+serviceId+"&customerAccountNumber="+accountNo+"&creditAccountNumber="+ccNo+"&holderFirstName="+fname+"&holderLastName="+lname+"&expireDate="+expire+"&provider="+provider+"";
                
                xmlHttp.open('GET', urlEndpoint, true);
                xmlHttp.send(null);
                xmlHttp.onreadystatechange = function(){
                    if (xmlHTTP.readyState == 4 && xmlHTTP.status == 200) {
                        var purchaseServiceResponse = JSON.parse(xmlHTTP.responseText);
                        if (purchaseServiceResponse.result == 'Approved' ) {
                            
                        }
                        else {
                            alert("Payment Rejected");
                        }
                    }
                    else {
                        alert("Error ->" + xmlhttp.responseText);
                    }
                };
            }
        </script>
    </head>
    <body>
    <c:if test="${requestType eq 'updateRequest'}">
    <form  id="BuyFlow" action="coherencedemo?requestType=updateRequest">
        <input type="hidden" name="action" id="action" value="Update">
        <input type="hidden" name="requestType" id="requestType" value="updateRequest">
        <input type="hidden" name="customerID" id="customerID" value="${customerId}">
        <!--<input type="submit" value="Submit">-->
    </form>
    <script>document.forms.namedItem("BuyFlow").submit();</script>
    </c:if>
    <div id="wrap">
    <div id="header"><img src="images/CoherenceImg.png" /></div>
    <c:if test="${requestType eq 'buyservice'}">
    <div id="main">
    <form name="demo" id="demo" action="coherencedemo?requestType=serviceBuyFlow" method="POST">
         <p>
          <label for="accountNo" class="field">Customer Account Number</label>
           
          <input type="text" name="accountNo" id="accountNo" value="${customerId}" required>
        </p>
        <p>
          <label for="serviceId" class="field">ServiceId</label>
           
          <select id="service" name="service" onclick="javascript: getSelectedServices();" required>
            <c:forEach var="service" items="${Services}">
              <option id="${service.serviceId}" value="${service.serviceId}">
                ${service.serviceName}
                -
                ${service.serviceId}
              </option>
            </c:forEach>
          </select>
        </p>
         <p>
          <label for="ccNo" class="field">Credit Card Number</label>
           
          <input type="text" name="ccNo" id="ccNo" value="" required>
        </p>
         <p>
          <label for="fname" class="field">First Name</label>
           
          <input type="text" name="fname" id="fname" value="" required>
        </p>
        <p>
          <label for="lname" class="field">Last Name</label>
           
          <input type="text" name="lname" id="lname" value="" required>
        </p>
        <p>
          <label for="expire" class="field">Expiration Date</label>
           
          <input type="text" name="expire" id="expire" value="" required>
        </p>
        <p>
          <label for="provider" class="field">Credit Card Type</label>
           
          <input type="text" name="provider" id="provider" value="" required>
        </p>
        <p>
        <input type="submit" value="Purchase Service">
        </p>
    </form>
    </div>
    </c:if>
    </div>
    </body>
</html>