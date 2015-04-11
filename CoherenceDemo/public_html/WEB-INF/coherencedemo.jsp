<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <title>Customer Self Service Portal</title>
     <link rel="stylesheet" type="text/css" href="css/style.css">

  <script>
      function getSelectedDevices() {
          var demoForm = document.forms.demo;
          var selectedValues = "";
          var x = 0;
          for (x = 0;x < demoForm.device.length;x++) {
              if (demoForm.device[x].selected) {
                  //alert(InvForm.kb[x].value);
                  selectedValues = demoForm.device[x].value + "," + selectedValues;
              }
          }
          demoForm.devicelist.value = selectedValues;  
      }

      function getSelectedServices() {
          var demoForm = document.forms.demo;
          var selectedValues = "";
          var x = 0;
          for (x = 0;x < demoForm.service.length;x++) {
              if (demoForm.service[x].selected) {
                  //alert(InvForm.kb[x].value);```````````````````````````````````````````````````````````````````````````````````````````````````````
                  selectedValues = demoForm.service[x].value + "," + selectedValues;
              }
          }

          demoForm.servicelist.value = selectedValues;
      }
      
      function buyService(){
            var demoForm = document.forms.demo;
            location.href='coherencedemo?requestType=buyservice&customerId=${custID}';
      }
    </script>
</head>
<body>
    <div id="wrap">
        <div id="header"><img src="images/CoherenceImg.png" /></div>
            <c:if test="${action eq 'Create'}"><form name="demo" id="demo" action="coherencedemo?requestType=create" method="POST"></c:if>
            <c:if test="${action eq 'Update'}"><form name="demo" id="demo" action="coherencedemo?requestType=update" method="POST"></c:if>
            <fieldset>
            <legend><c:if test="${action eq 'Create'}">Create Customer</c:if><c:if test="${action eq 'Update'}">Update Customer</c:if></legend>
            <div id="main">
  
    <p>
          <label for="custId" class="field">Customer ID</label>
           
          <input type="text" name="custId" id="custId" value="${custID}" readonly="true">
        </p>
         <p>
          <label for="accountNo" class="field">Account No</label>
           
          <input type="text" name="accountNo" id="accountNo" value="${accountNo}"  readonly="true">
        </p>
      
        <p>
          <label for="fname" class="field">First Name</label>
           
          <input type="text" name="fname" id="fname" value="${firstName}" required>
        </p>
        <p>
          <label for="lname" class="field">Last Name</label>
           
          <input type="text" name="lname" id="lname" value="${lastName}" required>
        </p>
         <p>
          <label for="telephone" class="field">Phone</label>
           
          <input type="text" name="telephone" id="telephone" value="${phone}" required>
        </p>
         <p>
          <label for="device" class="field">Device</label>
           
          <select id="device" name="device" multiple="multiple" onclick="javascript: getSelectedDevices();" required>
            <c:forEach var="device" items="${Devices}">
              <option value="${device.deviceId}" ${device.selected}>
                ${device.deviceMake}
                -
                ${device.deviceModel}
                -
                ${device.deviceId}
              </option>
            </c:forEach>
          </select>
        </p>
         <p>
          <input type="hidden" id="devicelist" name="devicelist"/>
           
      
        </p>
                <p>
        <c:if test="${action eq 'Create'}"><input type="Submit" value="Create Customer"/></c:if>
        </p>
    </div>
    <div id="sidebar">
    <p>
          <label for="address" class="field">Address</label>
           
          <input type="text" name="address" id="address" value="${address}" required>
        </p>
        <p>
          <label for="city" class="field">City</label>
           
          <input type="text" name="city" id="city" value="${city}" required>
        </p>
        <p>
          <label for="state" class="field">State</label>
           
          <input type="text" name="state" id="state" value="${state}" required>
        </p>
        <p>
          <label for="pin" class="field">Postal Code</label>
           
          <input type="text" name="pin" id="pin" value="${pinCode}" required>
        </p>
        <p>
          <label for="country" class="field">Country</label>
           
         
           
          <select id="country" name='country'>
            <c:forEach var="ctry" items="${Countries}">
              <option value="${ctry}"  <c:if test="${country eq ctry}">selected</c:if>>
                ${ctry}
              </option>
            </c:forEach>
          </select>
        </p>
       
         <p>
          <label for="service" class="field">Service</label>
           
          <select id="service" name="service" multiple="multiple" onclick="javascript: getSelectedServices();" required>
            <c:forEach var="service" items="${Services}">
                <option id="${service.serviceId}" value="${service.serviceId}">
                        ${service.serviceName}
                        -
                        ${service.serviceId}
                </option>
              <c:forEach var="custService" items="${customerServices}">
                <c:if test="${custService.serviceId eq service.serviceId}">
                    <script>document.getElementById("service").options.namedItem("${service.serviceId}").selected=true;</script>    
                </c:if>
              </c:forEach>
            </c:forEach>
          </select>
        </p>
        <p>
          <input type="hidden" id="servicelist" name="servicelist"/>
        </p>
        
        <c:if test="${action eq 'Update'}">
        <p>
        <input type="Submit" value="Update Customer"/>
        </p>
        <p>
        <input type="button" onclick="javascript: buyService();" value="Buy Service">
        </p>
        </c:if>
    </div>
    </fieldset>
       </form>
    <div id="footer">
    <p><a href="coherencedemo?requestType=login">Home</a> &copy; 2014  Copyright Oracle</p>
    </div>
    </div>


</body>
</html>