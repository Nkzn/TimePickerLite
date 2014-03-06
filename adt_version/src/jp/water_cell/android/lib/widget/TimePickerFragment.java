package jp.water_cell.android.lib.widget;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

public class TimePickerFragment extends Fragment {
	
	public interface OnTimeChangedListener {
		void onTimeChanged(TimePickerFragment fragment, int hourOfDay, int minute);
	}
	
	public static final String KEY_INTERVAL = "interval";
	
	public static final String KEY_INITIAL_HOUR = "initial_hour";
	
	public static final String KEY_INITIAL_MINUTE = "initial_minute";
	
	private int mCurrentHour = -1;
	
	private int mCurrentMinute = -1;
	
	private int mIntervalMinute;
	
	private NumberPicker mHourPicker;
	
	private NumberPicker mMinutePicker;
	
	private OnTimeChangedListener mListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle args = getArguments();
		if(args != null) {
			mIntervalMinute = args.getInt(KEY_INTERVAL, 0);
			if(mCurrentHour == -1) mCurrentHour = args.getInt(KEY_INITIAL_HOUR, -1);
			if(mCurrentMinute == -1) mCurrentMinute = args.getInt(KEY_INITIAL_MINUTE, -1);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.time_picker_holo, null);
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mHourPicker = (NumberPicker) view.findViewById(R.id.hour);
		mHourPicker.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				mCurrentHour = newVal;
				if(mListener != null) {
					mListener.onTimeChanged(TimePickerFragment.this, mCurrentHour, mCurrentMinute*mIntervalMinute);
				}
			}
		});
		mMinutePicker = (NumberPicker) view.findViewById(R.id.minute);
		mMinutePicker.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				mCurrentMinute = newVal;
				if(mListener != null) {
					mListener.onTimeChanged(TimePickerFragment.this, mCurrentHour, mCurrentMinute*mIntervalMinute);
				}
			}
		});		
		
		initNumberPickers();
		
		int hour = -1;
		int minute = -1;
		if(mCurrentHour >= 0 && mCurrentMinute >= 0) {
			hour = mCurrentHour;
			minute = mCurrentMinute;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			hour = cal.get(Calendar.HOUR_OF_DAY);
			minute = cal.get(Calendar.MINUTE);
		}
		
		setCurrentTime(hour, minute);
	}
	
	void initNumberPickers() {
		initNumberPicker(mHourPicker, 23, 0);
		initNumberPicker(mMinutePicker, 59, mIntervalMinute);
	}
	
	void initNumberPicker(NumberPicker np, int max, int _interval) {
		final int interval = _interval; 
		int internalMax = max;
		
		if(interval > 0) {
			internalMax = max / interval;
		}
		
		String[] displayedValues = new String[internalMax+1];
		int itvl = interval > 0 ? interval : 1;
		for(int i=0; i <= internalMax; i++) {
			String format = String.valueOf(i*itvl);
			if(format.length() == 1) format = "0"+format;
			displayedValues[i] = format;
		}
		
		np.setMinValue(0);
		np.setMaxValue(internalMax);
		np.setDisplayedValues(displayedValues);
	}
	
	public void setOnTimeChangedListener(OnTimeChangedListener listener) {
		mListener = listener;
	}
	
	public void setInterval(int intervalMinute) {
		mIntervalMinute = intervalMinute;
	}
	
	public void setCurrentTime(int hour, int minute) {
		int interval = (mIntervalMinute == 0) ? 1 : mIntervalMinute;
		
		mCurrentHour = hour;
		if(mHourPicker != null) {
			mHourPicker.setValue(mCurrentHour);
		}
		mCurrentMinute = minute / interval;
		if(mMinutePicker != null) {
			mMinutePicker.setValue(mCurrentMinute);
		}		
	}

	public int getCurrentHour() {
		return mHourPicker != null ? mHourPicker.getValue() : mCurrentHour;
	}
	
	public int getCurrentMinute() {
		return (mMinutePicker != null ? mMinutePicker.getValue() : mCurrentMinute) * ((mIntervalMinute==0)?1:mIntervalMinute);
	}
	
}
