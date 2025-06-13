@import com.henningstorck.springbootvite.frontend.Frontend

@param Frontend frontend

<html lang="en">
<head>
	<title>Hello, World!</title>
	<meta charset="UTF-8"/>

	@for(String stylesheet : frontend.stylesheets())
		<link rel="stylesheet" href="${stylesheet}"/>
	@endfor
</head>
<body>
<h1>Hello, World!</h1>

@for(String script : frontend.scripts())
	<script type="module" src="${script}"></script>
@endfor
</body>
</html>
