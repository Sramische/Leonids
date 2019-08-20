package com.plattysoft.leonids;

import java.util.List;

import android.graphics.*;
import com.plattysoft.leonids.modifiers.ParticleModifier;

public class Particle {

	protected Bitmap mImage;

	public float mCurrentX;
	public float mCurrentY;

	public float mScale = 1f;
	public int mAlpha = 255;

	public float mInitialRotation = 0f;

	public float mRotationSpeed = 0f;

	public float mSpeedX = 0f;
	public float mSpeedY = 0f;

	public float mAccelerationX;
	public float mAccelerationY;

	private Matrix mMatrix;
	private Paint mPaint;

	private float mInitialX;
	private float mInitialY;

	private float mRotation;

	private long mTimeToLive = 10000;

	protected long mStartingMilisecond;

	private int mBitmapHalfWidth;
	private int mBitmapHalfHeight;

	private List<ParticleModifier> mModifiers;

	public Rect validArea;

	protected Particle() {
		mMatrix = new Matrix();
		mPaint = new Paint();
	}

	public Particle (Bitmap bitmap) {
		this();
		mImage = bitmap;
	}

	public void init() {
		mScale = 1;
		mAlpha = 255;
	}

	public void setPosition(float emiterX, float emiterY) {
		mBitmapHalfWidth = mImage.getWidth()/2;
		mBitmapHalfHeight = mImage.getHeight()/2;

		mInitialX = emiterX - mBitmapHalfWidth;
		mInitialY = emiterY - mBitmapHalfHeight;
		mCurrentX = mInitialX;
		mCurrentY = mInitialY;
	}

	public boolean update (long miliseconds) {
		long realMiliseconds = miliseconds - mStartingMilisecond;
		if (realMiliseconds > mTimeToLive) {
			return false;
		}
		mCurrentX = mInitialX+mSpeedX*realMiliseconds+mAccelerationX*realMiliseconds*realMiliseconds;
		mCurrentY = mInitialY+mSpeedY*realMiliseconds+mAccelerationY*realMiliseconds*realMiliseconds;

		if(validArea !=null && !validArea.contains((int)mCurrentX, (int)mCurrentY))
			return false;

		//mRotation = mInitialRotation + mRotationSpeed*realMiliseconds/1000;

		for (int i=0; i<mModifiers.size(); i++) {
			mModifiers.get(i).apply(this, realMiliseconds);
		}

		return true;
	}

	public void draw (Canvas c) {
		mMatrix.reset();
		if(mRotation != 0)
			mMatrix.postRotate(mRotation, mBitmapHalfWidth, mBitmapHalfHeight);
		if(mScale != 1)
			mMatrix.postScale(mScale, mScale, mBitmapHalfWidth, mBitmapHalfHeight);
		mMatrix.postTranslate(mCurrentX, mCurrentY);
		mPaint.setAlpha(mAlpha);
		c.drawBitmap(mImage, mMatrix, mPaint);
	}

	public Particle activate(long startingMilisecond, List<ParticleModifier> modifiers) {
		mStartingMilisecond = startingMilisecond;
		// We do store a reference to the list, there is no need to copy, since the modifiers do not carte about states 
		mModifiers = modifiers;
		return this;
	}
}
