package edu.cmu.hcii.peer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.cmu.hcii.novo.kadarbra.R;
import edu.cmu.hcii.peer.structure.Procedure;
import edu.cmu.hcii.peer.util.FontManager;
import edu.cmu.hcii.peer.util.FontManager.FontStyle;

/**
 * 
 * A widget that displays the previous and next step with a one-line message.
 *
 * @author Chris
 *
 */
public class StepPreviewWidget extends LinearLayout{

	
	public StepPreviewWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater inflater = LayoutInflater.from(context);
        View page = (View)inflater.inflate(R.layout.preview, null);

        this.addView(page);
        initFonts();
	}



	/**
	 * Setup the custom fonts for this view.
	 */
	private void initFonts() {
		FontManager fm = FontManager.getInstance(getContext().getAssets());
	
		((TextView)findViewById(R.id.leftText)).setTypeface(fm.getFont(FontStyle.BODY));
		((TextView)findViewById(R.id.rightText)).setTypeface(fm.getFont(FontStyle.BODY));
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
	 * @param pagerIndex the page within ViewPager that we want to short a preview of
	 * @param textView the TextView used to display the preview
	 */
	private void setStepText(Procedure procedure, int pagerIndex, TextView textView) {
		String preview = "";
		if (pagerIndex < ProcedureActivity.PREPARE_PAGES) {
			if (pagerIndex == 0) {
				preview = procedure.getTitle();
				
			} else if (pagerIndex == 1) {
				preview = getContext().getResources().getString(R.string.stow_note_preview);
				
			} else if (pagerIndex == 2) {
				preview = getContext().getResources().getString(R.string.ex_note_preview);
			}
			
		} else if (pagerIndex - ProcedureActivity.PREPARE_PAGES < procedure.getStepPreviewSize()) {
			preview = procedure.getStepPreview(pagerIndex - ProcedureActivity.PREPARE_PAGES);
		
		} else if (pagerIndex - ProcedureActivity.PREPARE_PAGES == procedure.getStepPreviewSize()) {
			preview = getContext().getResources().getString(R.string.completion_preview);
		}
			
		textView.setText(preview);
	}
	
}
