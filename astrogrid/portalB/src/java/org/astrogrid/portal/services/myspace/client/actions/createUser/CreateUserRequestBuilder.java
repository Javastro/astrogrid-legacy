
package org.astrogrid.portal.services.myspace.client.actions.createUser ;

/**
 * Class to encapsulate a copy request.
 *
 */
public class CreateUserRequestBuilder

	{

	/**
	 * Public constructor.
	 *
	 */
	public CreateUserRequestBuilder(String source, int extentionPeriod)
		{
		this.source = source ;
		this.extentionPeriod = extentionPeriod ;
		}

	/**
	 * Our source path.
	 *
	 */
	private String source ;

	/**
	 * extentionPeriod.
	 *
	 */
	private int extentionPeriod ;

	/**
	 * Get our request as a String containing XML.
	 *
	 */
	public String toString()
		{
			StringBuffer buffer = new StringBuffer() ;
			try{
				buffer.append(
					"<request>"
					) ;
				buffer.append(
					"<userID>extendleaseFrog</userID>"
					) ;
				buffer.append(
					"<communityID>extendleaseFrogs</communityID>"
					) ;
				buffer.append(
					"<jobID>0000</jobID>"
					) ;
				buffer.append(
					"<serverFileName>"
					) ;
				buffer.append(
					this.source
					) ;
				buffer.append(
					"</serverFileName>"
					) ;
				buffer.append(
					"<extentionPeriod>"
					) ;
				buffer.append(
					this.extentionPeriod
					) ;
				buffer.append(
					"</extentionPeriod>"
					) ;
				buffer.append(
					"</request>"
					) ;
			}catch(NumberFormatException nfe){
				System.out.println("Exception formating extentionPeriod in CreateUserRequestBuilder.toString: ");
				nfe.printStackTrace();
			}
		return buffer.toString() ;
		}
	}
