<head>
    <title>Blood Torrent</title>
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css" />
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
    <script>
        $(function() {
            $( "input[type=button]" ).button()
        });
        function goLogin() {
            var email = document.getElementById("email").value;
            var password = document.getElementById("password").value;

            document.login.action = "/login";
            document.login.method = "post";
            document.login.submit();
        }
    </script>
</head>
<body>
<h1><label id="title">Welcome to Blood Torrent</label></h1> <br/>
<#if user?exists>
Hello ${user.id}
<a href="/logoff">Sign off</a>
<#else>
<form name="login">
ID : <input type="text" name="email" id="email" value="" />&nbsp;&nbsp;&nbsp;&nbsp;
PWD : <input type="password" id="password" name="password" /> &nbsp;&nbsp;
<input type="button" value="Log in" name="login" onclick="goLogin();">
</form>
<br/>
<br/>
<a href="/user"><input type="button" value="Register donor"/></a> &nbsp;
<a href="/requestForBlood"><input type="button" value="Request blood"/></a>  <br/>
</#if>
</body>