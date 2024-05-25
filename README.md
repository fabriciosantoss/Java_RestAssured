# Raidiam Task

Hello folks, find below how you can setup and execute the project

<h1>Environment</h1>
First of all, to build and run my project, you should have following tools installed:</p>
<p>Java 17 Amazon Coretto</p>
<p>Maven</p>
<p>Git</p>

<h1>Running the tests</h1>
For running the tests you will need execute local in your computer, the project <a href="https://bitbucket.org/thiagohcn/customer-data-api-java/src/master/"> customer-data-api-java </a>. After that you should clone my project, acess the file src/main/java/tests/AccountTest.java, and execute it.

In the project have a file to CI/CD, but how it is running local, its not working yet.

<h1>API Documentation</h1>
You could add more informations about the API, for exemple insert the complete endpoint, the correct account's endpoint is "account/v1/accounts" but in the documentatios is mentioned "accounts", so in the first view is a little bite confuse.

<h1>Bugs</h1>

<h3>documentation</h2>
  - The Wrong path of the account's endpoint-> the correct account's endpoint is "account/v1/accounts" but in the documentatios is mentioned "accounts", This is a bug with priority medium, because this is more easy and simple to be resolved </br>

<h3>Functionality</h3> 
- Error 500 when i use one expired date -> When i use a consetID with a expired date to acess the accounts API, is returning status code 500, because the api is receving a null point. We could insert the same message that is shwoed in the consent api "Consent Id not present on the request" when we dont have a consentID, this is a bug with high priority, because its occurring a crash

<br>- Error 500 when i use consent with status rejected-> When i use a consetID with the rejected status to acess the accounts API, is returning status code 500, because the api is receving a null point. We could insert the same message that is shwoed in the consent api "Consent Id not present on the request" when we dont have a consentID</br>


<h1>Security Test PLan</h1>
<h3>Try acess with expired consent</h3> 
<b>Given</b> you have a consentID with expired date</p>
<b>When</b>  you try acess a account with this consentID</p>
<b>Then</b> you won't have acess</p>

<h3>try acess with consent rejected</h3> 
<b>Given</b> you have a consentID with status rejected</p>
<b>When</b> you try acess a account with this consentID</p>
<b>Then</b> you won't have acess</p>

<h3>Try acess with ConsentID invalid</h3> 
<b>Given</b> you have a consentID with a another partern</p>
<b>When</b> you try acess a account with this consentID</p>
<b>Then</b> you you won't have acess</p>

<h3>Try acess with a wrong type of ConsentID</h3> 
<b>Given</b> you have a consentID to create credit cards</p>
<b>When</b> you try acess a account with this consentID</p>
<b>Then</b> you you won't have acess</p>

<br>An improvement to the API would be to cancel the ConsetIDs already used, so if someone gets a ConsentID from one through leaks, they would not be able to access the account</br>

