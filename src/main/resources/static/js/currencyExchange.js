$(document).ready(function () {
   const apiRoot = "http://localhost:8080/v1/exchange/currency";

   function exchangeCurrency(event) {
       event.preventDefault();

       var currenyFrom = $(this).find('[name="currency"]').val();
       var currenyTo = $(this).find('[name="newCurrency"]').val();
       var amount = $(this).find('[name="price"]').val();

       console.log('The acutal curr is: ' + currenyFrom + ', conv to: ' + currenyTo + ', with am: ' + amount);
   }
});