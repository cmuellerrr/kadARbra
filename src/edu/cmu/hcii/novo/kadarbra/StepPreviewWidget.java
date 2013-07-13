package edu.cmu.hcii.novo.kadarbra;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.cmu.hcii.novo.kadarbra.structure.Procedure;
import edu.cmu.hcii.novo.kadarbra.structure.Step;

public class StepPreviewWidget extends LinearLayout{

	
	
	public StepPreviewWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater inflater = LayoutInflater.from(context);
        View page = (View)inflater.inflate(R.layout.preview, null);

        this.addView(page);
	}
	
	
	
	/**
	 * 
	 * @param procedure current procedure
	 * @param step current page within ViewPager
	 */
	public void setCurrentStep(Procedure procedure, int step){
		setStepText(procedure, step-1, (TextView)findViewById(R.id.leftText));
		setStepText(procedure, step+1, (TextView)findViewById(R.id.rightText));
	}
	
	/**
	 * 
	 * @param procedure current procedure
	 * @param step the page within ViewPager that we want to short a preview of
	 * @param textView the TextView used to display the preview
	 */
	private void setStepText(Procedure procedure, int step, TextView textView){
		String string = "";
		if (step < ProcedureActivity.PREPARE_PAGES){
			if (step == 0){
				string = "Microbiology Sample Collection";
			}else if (step == 1){
				string = "Please gather the following equipment...";
			}else if (step == 2){
				string = "Execution Notes";
			}
			
		}else if (step-ProcedureActivity.PREPARE_PAGES < procedure.getStepsUnnested().size()){
			Step currentStep = procedure.getStepsUnnested().get(step-ProcedureActivity.PREPARE_PAGES);
			string = currentStep.getNumber() + ": " + currentStep.getText();
			
		}
			
		textView.setText(string);
	}
	
}
