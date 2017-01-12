package es.caib.gusite.front.captcha;

import nl.captcha.text.producer.TextProducer;

public class GusiteTextProducer implements TextProducer {

	 /**
     * Key cifrada con el texto del captcha.
     */
    private final String key;

    /**
     * Constructor.
     * 
     * @param pKey
     *            Key cifrada con el texto del captcha.
     */
    public GusiteTextProducer(final String pKey) {
        key = pKey;
    }

    /*
     * (non-Javadoc)
     * 
     * @see nl.captcha.text.producer.TextProducer#getText()
     */
    @Override
    public String getText() {
        return key;
    }

}
