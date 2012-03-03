package core;


public class Neutrino {
	public static void main(String[] args) throws Exception {
        // Some idiot stuff, starting cores or something...
		try {
		System.out.println("Welcome to Neutrino Final Version! Developed by Itachi");
		Environment.Init();
		} catch (Exception e) {
			Environment.WriteException(e);
		}
    }
}
