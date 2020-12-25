package app.iot;

import android.app.Activity;

import java.util.Iterator;
import java.util.Stack;

import app.iot.ui.MainActivity;

public class ActivityStackManager {

    private Stack<Activity> stack;

    private ActivityStackManager() {
        stack = new Stack<>();
    }

    public static ActivityStackManager getInstance() {
        return Instance.INSTANCE;
    }

    public Activity getTopActivity() {
        return stack.isEmpty() ? null : stack.peek();
    }

    /**
     * @param activity 需要添加进栈管理的activity
     */
    public void addActivity(Activity activity) {
        stack.add(activity);
    }

    public MainActivity getMainActivity() {
        Iterator<Activity> iterator = stack.iterator();
        while (iterator.hasNext()) {
            Activity next = iterator.next();
            if (next instanceof MainActivity) {
                return (MainActivity) next;
            }
        }
        return null;
    }

    /**
     * @param activity 需要从栈管理中删除的activity
     * @return
     */
    public boolean removeActivity(Activity activity) {
        return stack.remove(activity);
    }

    /**
     * @param activity 查询指定activity在栈中的位置，从栈顶开始
     * @return
     */
    public int searchActivity(Activity activity) {
        return stack.search(activity);
    }

    /**
     * @param activity 将指定的activity从栈中删除然后finish()掉
     */
    public void finishActivity(Activity activity) {
        stack.pop().finish();
    }

    /**
     * @param activity 将指定类名的activity从栈中删除并finish()掉
     */
    public void finishActivityClass(Class<Activity> activity) {
        if (activity != null) {
            Iterator<Activity> iterator = stack.iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();
                if (next.getClass().equals(activity)) {
                    iterator.remove();
                    finishActivity(next);
                }
            }
        }
    }

    /**
     * 销毁所有的activity
     */
    public void finishAllActivity() {
        while (!stack.isEmpty()) {
            stack.pop().finish();
        }
    }

    private static class Instance {
        public static ActivityStackManager INSTANCE = new ActivityStackManager();
    }

}