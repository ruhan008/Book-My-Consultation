<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<p>Hi ${prescription.userName},</p>

<p>A prescription has been uploaded by Dr. ${prescription.doctorName} on the portal with regard to your appointment.<br>
We wish you a speedy recovery and also thank you for choosing us to serve you.</p?

<p> Please find the prescription details below.</p>

<p> Diagnosis: ${prescription.diagnosis} </p>

<p> Medicine List : <br>
<#list prescription.medicineList as medicine>
${medicine}<br>
</#list>
</p>

<p>Thanks & Regards,<br>
<em>BookMyConsultation</em><br>
</p>

</body>
</html>