package org.shipp.util;

import java.util.ArrayList;
import java.util.List;

import org.shipp.activity.PrincipalActivity;
import org.shipp.activity.R;
import org.shipp.model.Menu;

/**
 * Class to control of menu, add menu to array for add a new menu
 * @author Leonardo Salles
 * Code borrowed from specified author to satisfy the sliding menu feature of the app
 */
public class MenuConfigAdapter {
	
	/**
	 * Obtaining the default menu for the app
	 * @return
	 */
	public static List<Menu> getMenuDefault(){
		//Menu menu = new Menu(int id, int drawableIcon, String title, String description)
		Menu menu1 = new Menu(1, R.drawable.mycard, PrincipalActivity.activity.getString(R.string.item_opus), PrincipalActivity.activity.getString(R.string.item_opus));
		Menu menu2 = new Menu(2, R.drawable.user, PrincipalActivity.activity.getString(R.string.item_profile), PrincipalActivity.activity.getString(R.string.item_profile));
		Menu menu3 = new Menu(3, R.drawable.home, PrincipalActivity.activity.getString(R.string.item_myaccount), PrincipalActivity.activity.getString(R.string.item_myaccount));
		Menu menu4 = new Menu(4, R.drawable.several, PrincipalActivity.activity.getString(R.string.item_purchase), PrincipalActivity.activity.getString(R.string.item_purchase));
		Menu menu5 = new Menu(5, R.drawable.products, PrincipalActivity.activity.getString(R.string.item_usage), PrincipalActivity.activity.getString(R.string.item_usage));
		Menu menu6 = new Menu(6, R.drawable.bus_white, PrincipalActivity.activity.getString(R.string.item_bus), PrincipalActivity.activity.getString(R.string.item_bus));
		Menu menu7 = new Menu(7, R.drawable.subway_white, PrincipalActivity.activity.getString(R.string.item_metro), PrincipalActivity.activity.getString(R.string.item_metro));
		Menu menu8 = new Menu(8, R.drawable.cupons, PrincipalActivity.activity.getString(R.string.item_themes), PrincipalActivity.activity.getString(R.string.item_themes));
		Menu menu9 = new Menu(9, R.drawable.register, PrincipalActivity.activity.getString(R.string.item_register), PrincipalActivity.activity.getString(R.string.item_register));
		Menu menu10 = new Menu(10, R.drawable.fork, PrincipalActivity.activity.getString(R.string.item_about), PrincipalActivity.activity.getString(R.string.item_about));
		
		List<Menu> listMenu = new ArrayList<Menu>();
		listMenu.add(menu1);
		listMenu.add(menu2);
		listMenu.add(menu3);
		listMenu.add(menu4);
		listMenu.add(menu5);
		listMenu.add(menu6);
		listMenu.add(menu7);
		listMenu.add(menu8);
		listMenu.add(menu9);
		listMenu.add(menu10);

		return listMenu;
	}
}
