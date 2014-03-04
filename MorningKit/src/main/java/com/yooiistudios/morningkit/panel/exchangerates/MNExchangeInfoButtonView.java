package com.yooiistudios.morningkit.panel.exchangerates;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yooiistudios.morningkit.common.dp.DipToPixel;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

public class MNExchangeInfoButtonView extends Button
{
	public MNExchangeInfoButtonView(Context context) {
		super(context);
        if (!isInEditMode()) {
            init();
        }
	}

	public MNExchangeInfoButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
        if (!isInEditMode()) {
            init();
        }
	}

	public MNExchangeInfoButtonView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
        if (!isInEditMode()) {
            init();
        }
	}

	private void init()	{
		if(!staticInitialized)
			staticInitializing();

        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getContext());
        this.setBackgroundColor(MNSettingColors.getForwardBackgroundColor(currentThemeType));
//		this.setOnTouchListener( OnTouchColorChanger.getInst() );
		this.post(new Runnable() {
			@Override
			public void run() {
				int width = MNExchangeInfoButtonView.this.getWidth();
				int height = sizeH_Flag * 2;
				MNExchangeInfoButtonView.this.setLayoutParams( new LinearLayout.LayoutParams(width, height));
			}
		});
	}

	private void staticInitializing() {
		sizeW_Flag = DipToPixel.dpToPixel(getContext(), 50);
		sizeH_Flag = DipToPixel.dpToPixel(getContext(), 30);

		offset_Flag_x = DipToPixel.dpToPixel(getContext(), -20);
		offset_Flag_y = DipToPixel.dpToPixel(getContext(), 0);
		
		offset_CountryCode_x = DipToPixel.dpToPixel(getContext(), 30);
		offset_CountryCode_y = DipToPixel.dpToPixel(getContext(), 10);

		textSize_CurrencyCode = DipToPixel.dpToPixel(getContext(), 17);
		staticInitialized = true;
	}

	static private boolean staticInitialized = false;

	static int sizeW_Flag;
	static int sizeH_Flag;

	static int offset_Flag_x;
	static int offset_Flag_y;

	static int offset_CountryCode_x;
	static int offset_CountryCode_y;

	static int textSize_CurrencyCode;
	private Bitmap countryFlag;
	
	private String currencyCode;
	
	public void loadExchangeCountry(String currencyCode) {
		MNCurrencyInfo currency = MNCurrencyInfo.getCurrencyInfo(currencyCode);
        this.countryFlag = FlagBitmapFactory.getGrayscaledFlagBitmap(getContext(), currency.usingCountryCode);
        this.countryFlag = Bitmap.createScaledBitmap(countryFlag, sizeW_Flag, sizeH_Flag, true);

		this.currencyCode = currencyCode;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint paint = new Paint();
        if (!isInEditMode()) {
            paint.setColor(MNSettingColors.getMainFontColor(MNTheme.getCurrentThemeType(getContext())));
        }
		paint.setAntiAlias(true);
		paint.setTextAlign(Align.CENTER);

		int centerX = getWidth()/2;
		int centerY = getHeight()/2;

		int bitmapW = countryFlag.getWidth();
		int bitmapH = countryFlag.getHeight();
		canvas.drawBitmap(countryFlag,
                centerX + offset_Flag_x - bitmapW / 2,
				centerY + offset_Flag_y - bitmapH / 2, paint);
//		paint.setTypeface(CommonTypeface.get());
		paint.setTextSize(textSize_CurrencyCode);
		canvas.drawText(currencyCode,
				centerX + offset_CountryCode_x,
				centerY + offset_CountryCode_y, paint);
	}
}











