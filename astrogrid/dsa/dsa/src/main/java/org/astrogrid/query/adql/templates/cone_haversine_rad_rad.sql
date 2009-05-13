SELECT * FROM INSERT_NAME_CATALOG.INSERT_NAME_TABLE AS a WHERE 
   (
      (a."INSERT_NAME_DEC" <= INSERT_VALUE_MAX_DEC_RAD)  AND   
      (a."INSERT_NAME_DEC" >= INSERT_VALUE_MIN_DEC_RAD)  
   ) 
   INSERT_RA_CLIP_CONDITION
   AND  
   ( 
      (2.0 * ASIN(SQRT( 
         POWER(SIN(((a."INSERT_NAME_DEC" - 
               INSERT_VALUE_DEC_RAD) / 2.0)), 2)  +  
           ( 
              (COS(a."INSERT_NAME_DEC")  *  
                 (
                    COS(INSERT_VALUE_DEC_RAD)  * 
                    POWER(SIN(((a."INSERT_NAME_RA" - 
                        INSERT_VALUE_RA_RAD) / 2.0)), 2) 
                 )
              )  
           )  
      ) ) )  
      < INSERT_VALUE_CIRCRADIUS_RAD
  );
