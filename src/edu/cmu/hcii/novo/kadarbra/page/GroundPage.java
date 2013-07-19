/**
 * 
 */
package edu.cmu.hcii.novo.kadarbra.page;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import edu.cmu.hcii.novo.kadarbra.R;

/**
 * @author Chris
 *
 */
public class GroundPage extends LinearLayout implements DrawerPageInterface{

	/**
	 * @param context
	 */
	public GroundPage(Context context) {
		super(context);
		
		
		this.addView(LayoutInflater.from(context).inflate(R.layout.call_ground, null));
		
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	
	
	/**
	 * @param context
	 * @param attrs
	 */
	public GroundPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}



	/**
	 * Returns name of drawer type
	 */
	@Override
	public String getDrawerType() {
		return DrawerPageInterface.DRAWER_GROUND;
	}

	
	
}
