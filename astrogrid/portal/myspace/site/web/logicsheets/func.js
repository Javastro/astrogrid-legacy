      private String agp_utils_returnAnyParameter(String paramName, String defaultValue) {
        String result = null;
        
        Session session = null;
        if(request != null) {
          result = request.getParameter(paramName);
          if(result == null || result.length() == 0) {
            session = request.getSession(true);
          }
          else {
            return result;
          }
        }
        
        if(session != null) {
          result = (String) session.getAttribute(paramName);
  
          if(result != null &amp;&amp; result.length() > 0) {
            return result;
          }
        }
        
        return defaultValue;
      }

