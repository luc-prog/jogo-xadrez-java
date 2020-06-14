package boardgame;

public class ExcecaoTabuleiro extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ExcecaoTabuleiro(String msg){
		super(msg);
	}
}
