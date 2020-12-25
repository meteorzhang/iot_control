package app.iot.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by danbo on 2019-11-19.
 */
class ViewPagerAdapter(
    private val fm: FragmentManager,
    private val fragments: List<Fragment>
) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var fragment = super.instantiateItem(container, position) as Fragment
        val fragmentTag = fragment.tag
        if(fragment != getItem(position)){
            val ft = fm.beginTransaction()
            ft.remove(fragment)
            fragment = getItem(position)
            ft.add(container.id,fragment,fragmentTag)
            ft.attach(fragment)
            ft.commitAllowingStateLoss()
        }
        return fragment
    }
}
