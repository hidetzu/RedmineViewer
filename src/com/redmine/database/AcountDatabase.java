package com.redmine.database;

import com.redmine.data.Acount;

public interface AcountDatabase {
	public void setAcount(int id, String serverURL, String name, String password);
	public Acount getAcount(int id);
}
