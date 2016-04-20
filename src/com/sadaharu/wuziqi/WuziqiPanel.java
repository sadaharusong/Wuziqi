package com.sadaharu.wuziqi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.R.integer;
import android.R.string;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class WuziqiPanel extends View
{
	private int mPanelWidth;
	private float mLineHeight;
	private int MAX_LINE = 10;
	private int MAX_COUNT_IN_LINE = 5;

	private Paint mPaint = new Paint();

	private Bitmap mWhitePiece;
	private Bitmap mBlackPiece;

	private float ratioPieceOfLineHeight = 3 * 1.0f / 4;

	// 是否是白棋为当前手
	private boolean mIsWhite = false;
	private ArrayList<Point> mWhiteArray = new ArrayList<>();
	private ArrayList<Point> mBlackArray = new ArrayList<>();
	
	private boolean mIsGameOver;
	private boolean mIsWhiteWinner;

	public WuziqiPanel(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub

		setBackgroundColor(0x44ffd306);
		init();
	}

	private void init()
	{
		// TODO Auto-generated method stub
		mPaint.setColor(0x88000000);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Paint.Style.STROKE);

		mWhitePiece = BitmapFactory.decodeResource(getResources(),
				R.drawable.stone_w2);
		mBlackPiece = BitmapFactory.decodeResource(getResources(),
				R.drawable.stone_b1);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// TODO Auto-generated method stub
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		int width = Math.min(widthSize, heightSize);

		if (widthMode == MeasureSpec.UNSPECIFIED)
		{
			width = heightSize;
		} else if (heightMode == MeasureSpec.UNSPECIFIED)
		{
			width = widthSize;
		}
		setMeasuredDimension(width, width);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);

		mPanelWidth = w;
		mLineHeight = mPanelWidth * 1.0f / MAX_LINE;

		int pieceWidth = (int) (mLineHeight * ratioPieceOfLineHeight);

		mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth,
				pieceWidth, false);
		mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth,
				pieceWidth, false);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		drawBoard(canvas);

		drawPieces(canvas);
		
		checkGameOver();
	}

	private void checkGameOver()
	{
		// TODO Auto-generated method stub
		boolean whiteWin = checkFiveInLine(mWhiteArray);
		boolean blackWin = checkFiveInLine(mBlackArray);
		
		if (blackWin || whiteWin)
		{
			mIsGameOver = true;
			mIsWhite = whiteWin;
			
			String text = mIsWhiteWinner ? "白棋胜利" : "黑棋胜利";
			
			Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
		}
	}

	private boolean checkFiveInLine(List<Point> points)
	{
		for (Point p : points)
		{
			int x = p.x;
			int y = p.y;
			
			//！！！！！！！！！！为了快速实现，没有把这里面的东西规划
			//应当把4个check放在一个工具类，直接判断一次win比较好
			//优化后代码会没有现在冗余~
			boolean win =  checkHorizontal(x , y ,points);
			if (win)
			{
				return true;
			}
			win =  checkVetical(x, y, points);
			if (win)
			{
				return true;
			}
			win =  checkLeftDiagonal(x, y, points);
			if (win)
			{
				return true;
			}
			win =  checkRightDiagonal(x, y, points);
			if (win)
			{
				return true;
			}
		}
		// TODO Auto-generated method stub
		return false;
	}

	//判断X,Y位置的棋子是否有横向有相邻的五个一致
	private boolean checkHorizontal(int x, int y, List<Point> points)
	{
		int count = 1;
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++)
		{
			if (points.contains(new Point(x - i ,y)))
			{
				count++;
			}else {
				break;
			}
		}
		
		if (count == MAX_COUNT_IN_LINE)
		{
			return true;
		}
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++)
		{
			if (points.contains(new Point(x + i ,y)))
			{
				count++;
			}else {
				break;
			}
		}
		
		if (count == MAX_COUNT_IN_LINE)
		{
			return true;
		}
		return false;
		
	}
	
	private boolean checkVetical(int x, int y, List<Point> points)
	{
		int count = 1;
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++)
		{
			if (points.contains(new Point(x ,y - i)))
			{
				count++;
			}else {
				break;
			}
		}
		
		if (count == MAX_COUNT_IN_LINE)
		{
			return true;
		}
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++)
		{
			if (points.contains(new Point(x  ,y + i)))
			{
				count++;
			}else {
				break;
			}
		}
		
		if (count == MAX_COUNT_IN_LINE)
		{
			return true;
		}
		return false;
		
	}
	
	private boolean checkLeftDiagonal(int x, int y, List<Point> points)
	{
		int count = 1;
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++)
		{
			if (points.contains(new Point(x - i ,y + i)))
			{
				count++;
			}else {
				break;
			}
		}
		
		if (count == MAX_COUNT_IN_LINE)
		{
			return true;
		}
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++)
		{
			if (points.contains(new Point(x + i ,y - i)))
			{
				count++;
			}else {
				break;
			}
		}
		
		if (count == MAX_COUNT_IN_LINE)
		{
			return true;
		}
		return false;
		
	}
	
	private boolean checkRightDiagonal(int x, int y, List<Point> points)
	{
		int count = 1;
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++)
		{
			if (points.contains(new Point(x - i ,y - i)))
			{
				count++;
			}else {
				break;
			}
		}
		
		if (count == MAX_COUNT_IN_LINE)
		{
			return true;
		}
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++)
		{
			if (points.contains(new Point(x + i ,y + i)))
			{
				count++;
			}else {
				break;
			}
		}
		
		if (count == MAX_COUNT_IN_LINE)
		{
			return true;
		}
		return false;
		
	}

	private void drawPieces(Canvas canvas)
	{
		// TODO Auto-generated method stub
		for (int i = 0, n = mWhiteArray.size(); i < n; i++)
		{
			Point whitePoint = mWhiteArray.get(i);
			canvas.drawBitmap(mWhitePiece,
					(whitePoint.x + (1 - ratioPieceOfLineHeight) / 2)
							* mLineHeight,
					(whitePoint.y + (1 - ratioPieceOfLineHeight) / 2)
							* mLineHeight, null);
		}
		
		for (int i = 0, n = mBlackArray.size(); i < n; i++)
		{
			Point blackPoint = mBlackArray.get(i);
			canvas.drawBitmap(mBlackPiece,
					(blackPoint.x + (1 - ratioPieceOfLineHeight) / 2)
							* mLineHeight,
					(blackPoint.y + (1 - ratioPieceOfLineHeight) / 2)
							* mLineHeight, null);
		}
	}

	private void drawBoard(Canvas canvas)
	{
		// TODO Auto-generated method stub
		int w = mPanelWidth;
		float lineHeight = mLineHeight;

		for (int i = 0; i < MAX_LINE; i++)
		{
			int startX = (int) (lineHeight / 2);
			int endX = (int) (w - (lineHeight / 2));

			int y = (int) ((0.5 + i) * lineHeight);
			// 画横线
			canvas.drawLine(startX, y, endX, y, mPaint);
			// 画纵线
			canvas.drawLine(y, startX, y, endX, mPaint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (mIsGameOver)
		{
			return false;
		}
		int action = event.getAction();
		if (action == MotionEvent.ACTION_UP)
		{
			int x = (int) event.getX();
			int y = (int) event.getY();

			Point p = getValidPoint(x, y);

			if (mWhiteArray.contains(p) || mBlackArray.contains(p))
			{
				return false;
			}

			if (mIsWhite)
			{
				mWhiteArray.add(p);
			} else
			{
				mBlackArray.add(p);
			}
			invalidate();
			mIsWhite = !mIsWhite;

		}
		// TODO Auto-generated method stub
		return true;
	}

	private Point getValidPoint(int x, int y)
	{
		// TODO Auto-generated method stub
		return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
	}
	
	
	//View的存储与恢复
	private static final String INSTANCE = "instance";
	private static final String INSTANCE_GAME_OVER = "instance_game_over";
	private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
	private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";
	
	
	//存入棋局
	
	@Override
	protected Parcelable onSaveInstanceState()
	{
		Bundle bundle = new Bundle();
		bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
		bundle.putBoolean(INSTANCE_GAME_OVER, mIsGameOver);
		bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, mWhiteArray);
		bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, mBlackArray);
		// TODO Auto-generated method stub
		return bundle;
	}
	
	//如果Activity被结束了，还可以调用棋局
	@Override
	protected void onRestoreInstanceState(Parcelable state)
	{
		if (state instanceof Bundle)
		{
			Bundle bundle = (Bundle) state;
			mIsGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
			mWhiteArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
			mBlackArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
			super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
			
		
			
		}
		super.onRestoreInstanceState(state);
	}
	
	public void start()
	{
		mWhiteArray.clear();
		mBlackArray.clear();
		mIsGameOver = false;
		mIsWhiteWinner = false;
		invalidate();
	}
}
