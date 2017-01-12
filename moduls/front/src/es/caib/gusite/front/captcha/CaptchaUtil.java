package es.caib.gusite.front.captcha;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;



import nl.captcha.Captcha;
import nl.captcha.Captcha.Builder;
import nl.captcha.audio.AudioCaptcha;
import nl.captcha.audio.producer.RandomNumberVoiceProducer;
import nl.captcha.audio.producer.VoiceProducer;
import nl.captcha.text.producer.TextProducer;


/**
 * Utilidades captcha.
 * 
 * @author Indra
 * 
 */
public final class CaptchaUtil {

    /**
     * Letras que pueden formar parte del captcha. Tienen que ser numeros por
     * limitacion del sonido del captcha.
     */
    private static final String[] LETRAS = {"0", "1", "2", "3", "4", "5", "6",
            "7", "8", "9"};

    /**
     * Constructor.
     */
    private CaptchaUtil() {
        super();
    }

    /**
     * Genera imagen captcha.
     * 
     * @param value
     *            Valor captcha
     * @return Imagen captcha
     */
    public static ImagenCaptcha generaCaptcha(final String value) {

        byte[] contenido = null;
        final ImagenCaptcha res = new ImagenCaptcha();
        try {
        	
    		final Captcha captcha = creaCaptcha(value);
     
            final ByteArrayOutputStream bos = new ByteArrayOutputStream(ConstantesNumero.N4000);
            ImageIO.write(captcha.getImage(), "png", bos);
            contenido = bos.toByteArray();
            bos.close();
            
//            res.setFichero("captcha.png");
            res.setContenido(contenido);
                      
        } catch (final IOException io) {
            contenido = null;
        }

        return res;

    }

	/**
	 * @param value
	 * @return
	 */
	public static Captcha creaCaptcha(final String value) {
		final TextProducer tp = new GusiteTextProducer(value);
		final Builder builder = new Captcha.Builder(ConstantesNumero.N120,
		        ConstantesNumero.N50);
		final Captcha captcha = builder.addText(tp).gimp().addBorder()
		        .addNoise().addBackground().build();
		return captcha;
	}

    /**
     * Crea key para captcha.
     * 
     * @return key captcha
     */
    public static String generarKeyCaptcha() {
        final SecureRandom sr = new SecureRandom();
        final StringBuffer sb = new StringBuffer(ConstantesNumero.N10);
        for (int i = 0; i < ConstantesNumero.N4; i++) {
            final int numChar = sr.nextInt(LETRAS.length);
            sb.append(LETRAS[numChar]);
        }
        return sb.toString();
    }

    /**
     * Genera sonido captcha.
     * 
     * @param pValor
     *            valor captcha
     * @param pIdioma 
     * 			   idioma
     * @return Sonido captcha
     */
    public static SonidoCaptcha generaCaptchaSound(final String pValor, final String pIdioma) {
 	
    	
    	final Map<Integer, String[]> voicesMap = new HashMap<Integer, String[]>();
    	final String[] fileLocs0 = {"/sounds/"+pIdioma+"/0.wav", "/sounds/"+pIdioma+"/0.wav"};
    	final String[] fileLocs1 = {"/sounds/"+pIdioma+"/1.wav", "/sounds/"+pIdioma+"/1.wav"};
    	final String[] fileLocs2 = {"/sounds/"+pIdioma+"/2.wav", "/sounds/"+pIdioma+"/2.wav"};
    	final String[] fileLocs3 = {"/sounds/"+pIdioma+"/3.wav", "/sounds/"+pIdioma+"/3.wav"};
    	final String[] fileLocs4 = {"/sounds/"+pIdioma+"/4.wav", "/sounds/"+pIdioma+"/4.wav"};
    	final String[] fileLocs5 = {"/sounds/"+pIdioma+"/5.wav", "/sounds/"+pIdioma+"/5.wav"};
    	final String[] fileLocs6 = {"/sounds/"+pIdioma+"/6.wav", "/sounds/"+pIdioma+"/6.wav"};
    	final String[] fileLocs7 = {"/sounds/"+pIdioma+"/7.wav", "/sounds/"+pIdioma+"/7.wav"};
    	final String[] fileLocs8 = {"/sounds/"+pIdioma+"/8.wav", "/sounds/"+pIdioma+"/8.wav"};
    	final String[] fileLocs9 = {"/sounds/"+pIdioma+"/9.wav", "/sounds/"+pIdioma+"/9.wav"};
    	
    	
    	 voicesMap.put(0, fileLocs0);
    	 voicesMap.put(1, fileLocs1);
    	 voicesMap.put(2, fileLocs2);
    	 voicesMap.put(3, fileLocs3);
    	 voicesMap.put(4, fileLocs4);
    	 voicesMap.put(5, fileLocs5);
    	 voicesMap.put(6, fileLocs6);
    	 voicesMap.put(7, fileLocs7);
    	 voicesMap.put(8, fileLocs8);
    	 voicesMap.put(9, fileLocs9);
    	 
    	 
    	  final VoiceProducer vProd = new RandomNumberVoiceProducer(voicesMap);
    	 
    	  
    	  final TextProducer tp = new GusiteTextProducer(pValor);
    	 
		final AudioCaptcha ac = new AudioCaptcha.Builder()
    	     .addAnswer(tp)
    	    .addVoice(vProd)
    	    .addNoise()
    	    .build();

		 final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		 
		try{
		 final AudioInputStream audioInputStream = ac.getChallenge().getAudioInputStream();
		 AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, byteOut);
		 audioInputStream.close();
		 byteOut.close();
		}catch(final Exception ex){
			ex.printStackTrace();
		}

 

		final SonidoCaptcha res = new SonidoCaptcha();
        res.setFichero("captcha.wav");
        res.setContenido((byteOut).toByteArray());
        return res;
        
    }

}
