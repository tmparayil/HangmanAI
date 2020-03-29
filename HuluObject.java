class HuluObject
{
	private int remaining_guesses;
	private String token;
	private String state;
	private String status;

	HuluObject(int remaining_guesses,String token,String state,String status)
	{
		this.remaining_guesses = remaining_guesses;
		this.state = state;
		this.status = status;
		this.token = token;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getState()
	{
		return this.state;
	}

	public String getToken()
	{
		return this.token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public int getGuesses()
	{
		return this.remaining_guesses;
	}

	public void setGuesses(int remaining_guesses)
	{
		this.remaining_guesses = remaining_guesses;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getStatus()
	{
		return this.status;
	}

}