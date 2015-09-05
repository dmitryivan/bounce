package com.authorwjf.bounce;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class AnimatedView extends ImageView{

	private Context mContext;
	final int max=5;
    int[] xa=new int [max];
	int[] ya=new int [max];
	int[] xv=new int [max];
	int[] yv=new int [max];
	int[] colis=new int [max];
	
	static int touchBall=-1;
	static Handler h;
	final int FRAME_RATE = 50;
	int evX, evY;
	static int screenHeight,screenWidth,ballWidth,ballHeight;
	int i,j;
	
	public AnimatedView(Context context, AttributeSet attrs)  {  
		super(context, attrs);  
		mContext = context;  
		h = new Handler();
		createBalls();
	} 
	
	void createBalls(){
		Random r=new Random();
		for (int i=0; i<max; i++){
			xa[i]=100*i+10;
			ya[i]=100;
			xv[i]=r.nextInt(5);
			yv[i]=r.nextInt(5);
		}
	}
	
	Runnable r = new Runnable() {
		@Override
		public void run() {invalidate();}
	};
	
	void checkColis(){	
        for (int i=0;i<max;i++){
            colis[i]=-1;
        }
		
	for (int j=0; j<max; j++){
	   for (int i=0; i<max; i++){
	       if (i!=j && calcDist(xa[i]+xv[i],ya[i]+yv[i],xa[j]+xv[j],ya[j]+yv[j])<ballWidth){
				//	if (xa[i]-xa[j]>0 && xa[i]-xa[j] < ballWidth)  xa[i]=xa[j]+ballWidth;
				//	if (xa[j]-xa[i]>0 && xa[j]-xa[i] < ballWidth)  xa[i]=xa[j]-ballWidth;
				//	if (ya[i]-ya[j]>0 && ya[i]-ya[j] < ballWidth)  ya[i]=ya[j]+ballWidth;				
				//	if (ya[j]-ya[i]>0 && ya[j]-ya[i] < ballWidth)  ya[i]=ya[j]-ballWidth;
				
               xv[i]=(xv[i]*7+xv[j])/-8;	
	       yv[i]=(yv[i]*7+yv[j])/-8;
				//	xv[i]=xv[i]*-1;
				//	yv[i]=yv[i]*-1;
	       }
	  }
     }
}
		
	void checkBorders(){
		if (xa[i] >= screenWidth - ballWidth/2) 
		{
			xa[i]=screenWidth-ballWidth/2-1;
			xv[i] = xv[i]*-1;
		}
		if (xa[i] <=ballWidth/2)  
		{
			xa[i]=ballWidth/2;
			xv[i] = xv[i]*-1;
		}
		if (ya[i] >= screenHeight-ballHeight/2) 
		{
    	ya[i]=screenHeight-ballHeight/2-1;
			yv[i] = yv[i]*-1;
		}
		if (ya[i] <=ballHeight/2)  
		{
			ya[i]=ballHeight/2;
			yv[i] = yv[i]*-1;
		}
	}
	
	int findBall(int x, int y)
	{
		int ballNum=0;
		for (int i=0; i<max; i++)
		{
		if (calcDist(x,y, xa[i],ya[i])<=ballWidth & calcDist(x,y, xa[i],ya[i])<=calcDist(x,y,xa[ballNum],ya[ballNum])) ballNum=i;
		}
		return ballNum;
	}
	
	int calcDist (int x1, int y1, int x2, int y2){
		return(int)Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
	}
	
	protected void onDraw(Canvas c) {  
	    final BitmapDrawable ball = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.ball);  
	    screenWidth= this.getWidth();
        screenHeight=this.getHeight();
	    ballWidth=ball.getBitmap().getWidth();
	    ballHeight=ball.getBitmap().getHeight();
		

		for (i=0; i<max; i++){
		    checkColis();
			if (i!=touchBall) {xa[i] += xv[i];  ya[i] += yv[i];}

	    //	checkColis();	
    		checkBorders(); 
    		c.drawBitmap(ball.getBitmap(), xa[i]-ballHeight/2, ya[i]-ballWidth/2, null);	
		}
		
		h.postDelayed(r, FRAME_RATE);
	}
	   	      
	 
	
	@Override
	public boolean onTouchEvent (MotionEvent event)
	{   
		evX = (int)event.getX();
		evY = (int)event.getY();

		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:	
				touchBall=findBall(evX,evY);
				xa[touchBall]=evX;
				ya[touchBall]=evY;
				break;
				
			case MotionEvent.ACTION_MOVE:  
				xv[touchBall]=(evX-xa[touchBall])/2;
				yv[touchBall]=(evY-ya[touchBall])/2;
				xa[touchBall]=evX;
				ya[touchBall]=evY;
				break;

			case MotionEvent.ACTION_UP: 
				xa[touchBall]=evX;
				ya[touchBall]=evY;
				touchBall=-1;
				break;			
		}

	    invalidate(); 
	    return true;
	}
}
