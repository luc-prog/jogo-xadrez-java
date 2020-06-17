package xadrez;

import boardgame.Peca;
import boardgame.Posicao;
import boardgame.Tabuleiro;

public abstract class PecaXadrez extends Peca{
	
	private Cor cor;
	private int contarMovimento;

	public PecaXadrez(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}
	
	public PosicaoXadrez getChessPosition() {
		return PosicaoXadrez.fromPosition(posicao);
	}
	
	public int getContarMovimento() {
		return contarMovimento;
	}
	
	public void aumentarContagemMovimento() {
		contarMovimento++;
	}
	
	public void diminuirContagemMovimento() {
		contarMovimento++;
	}
	
	protected boolean existePecaOponente(Posicao posicao) {
		PecaXadrez p = (PecaXadrez)getTabuleiro().peca(posicao);
		return p != null && p.getCor() != cor;
	}
}
