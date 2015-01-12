
/**
 * AutomaticTranslationServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:24 EDT)
 */

    package es.caib.gusite.microintegracion.ws.traductor;

    /**
     *  AutomaticTranslationServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class AutomaticTranslationServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public AutomaticTranslationServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public AutomaticTranslationServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for test method
            * override this method for handling normal response from test operation
            */
           public void receiveResulttest(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from test operation
           */
            public void receiveErrortest(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for translate method
            * override this method for handling normal response from translate operation
            */
           public void receiveResulttranslate(
        		   es.caib.gusite.microintegracion.ws.traductor.AutomaticTranslationServiceStub.TaskE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from translate operation
           */
            public void receiveErrortranslate(java.lang.Exception e) {
            }
                


    }
    