
msg="The file has been attached."
Response.sendRedirect("${Context.getLastResolvedObject().getUrlPath()}?msg=${msg}")