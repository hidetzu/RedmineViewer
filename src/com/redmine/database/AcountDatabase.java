package com.redmine.database;

import com.redmine.Acount;

public interface AcountDatabase {
	public void setAcount(int id, String serverURL, String name, String password);
	public Acount getAcount(int id);
}
