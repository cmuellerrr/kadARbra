/**
 * 
 */
package edu.cmu.hcii.novo.kadarbra.page;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import edu.cmu.hcii.novo.kadarbra.ViewFactory;
import edu.cmu.hcii.novo.kadarbra.structure.Cycle;
import edu.cmu.hcii.novo.kadarbra.structure.ProcedureItem;
import edu.cmu.hcii.novo.kadarbra.structure.Step;

/**
 * A layout for a navigation page.  Displays all parent 
 * steps.
 * 
 * @author Chris
 *
 */
public class NavigationPage extends LinearLayout implements DrawerPageInterface {

	private List<ProcedureItem> steps;
	private int curStepIndex;
	
	/**
	 * @param context
	 */
	public NavigationPage(final Context context, List<ProcedureItem> steps, int curStepIndex) {
		super(context);
		this.setOrientation(VERTICAL);
		
		this.steps = steps;
		this.curStepIndex = curStepIndex;

		//The list is 1 based
		//TODO this whole indexing is kind of f-ed.  Shouldn't we just use the 
		//actual step number strings?
		int stepNumber = 1;
		for (int i = 0; i < steps.size(); i++) {
			if (steps.get(i).isCycle()) {
				Cycle c = (Cycle)steps.get(i);
				this.addView(ViewFactory.getNavigationCycle(context, curStepIndex, stepNumber, c));
				stepNumber += c.getNumChildren();
				
			} else {
				this.addView(ViewFactory.getNavigationStep(context, curStepIndex, stepNumber, (Step)steps.get(i), 1));
				stepNumber++;
			}
		}
		
		this.setGravity(Gravity.CENTER);
	}

	
	
	/**
	 * @param context
	 * @param attrs
	 */
	public NavigationPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	
	
	/**
	 * @return the steps
	 */
	public List<ProcedureItem> getSteps() {
		return steps;
	}



	/**
	 * @param steps the steps to set
	 */
	public void setSteps(List<ProcedureItem> steps) {
		this.steps = steps;
	}


	/**
	 * Returns type of interface
	 */
	@Override
	public String getDrawerType() {
		return DrawerPageInterface.DRAWER_NAVIGATION;
	}
}
