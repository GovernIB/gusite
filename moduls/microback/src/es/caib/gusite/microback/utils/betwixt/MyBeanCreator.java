package es.caib.gusite.microback.utils.betwixt;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.betwixt.io.read.BeanCreationChain;
import org.apache.commons.betwixt.io.read.ChainedBeanCreator;
import org.apache.commons.betwixt.io.read.ElementMapping;
import org.apache.commons.betwixt.io.read.ReadContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MyBeanCreator implements ChainedBeanCreator
{
	private static Log log = LogFactory.getLog( MyBeanCreator.class );
	public Object create(ElementMapping elementMapping, ReadContext readContext, BeanCreationChain arg2)
	{

		if ( elementMapping.getType().equals( Set.class ))
		{
			//return new HashSet(0);
			elementMapping.setType( HashSet.class );
		}
		
		Object objToReturn = arg2.create( elementMapping, readContext );
		//log.info( "CLM INPUT:" + elementMapping.getName() + ":" + elementMapping.getType().getName() );
		if ( objToReturn != null )
		{
			//log.info( "CLM RETURN:" + objToReturn.getClass().getName() );
		}
		else
		{
			//log.info( "CLM No se mapear " + elementMapping.getName() );
		}
		return objToReturn;
	}

}
