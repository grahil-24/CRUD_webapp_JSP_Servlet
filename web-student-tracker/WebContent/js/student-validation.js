function validateForm(){
	
	//error fields
	var theErrorFields = [];
	
	//the student form
	var studentForm = document.forms["studentForm"];
	
	//check firstName 
	var firstName = studentForm["firstName"].value.trim();
	if(firstName == ""){
		theErrorFields.push("First Name");
	}
	//check lastName
	var lastName = studentForm["lastName"].value.trim();
	if(lastName == ""){
		theErrorFields.push("Last Name");
	}
	//check email
	var email = studentForm["email"].value.trim();
	if(email == ""){
		theErrorFields.push("Email");
	}
	
	if(theErrorFields.length > 0){
		alert("Form validation failed. Please add data for following fields: "+theErrorFields);
		return false;
	}
}