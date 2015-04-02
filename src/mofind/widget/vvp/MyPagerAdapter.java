package mofind.widget.vvp;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;


public class MyPagerAdapter extends PagerAdapter {

	//界面列表
    private List<View> views;
    
    public MyPagerAdapter(List<View> views){
        this.views = views;
    }

	@Override
	public int getCount() {
		if (views != null) {
            return views.size();
        }      
        return 0;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(views.get(position), 0);
	       
        return views.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
