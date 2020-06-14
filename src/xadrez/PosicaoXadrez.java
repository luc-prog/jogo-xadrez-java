package xadrez;

import boardgame.Posicao;

public class PosicaoXadrez {
	
	private char coluna;
	private int linha;
	
	public PosicaoXadrez(char coluna, int linha) {
		if (coluna < 'a' || coluna > 'h' || linha < 1 || linha > 8) {
			throw new ExcecaoXadrez("Erro ao instanciar a posi��o. Os valores v�lidos s�o de a1 a h8.");
		}
		this.coluna = coluna;
		this.linha = linha;
	}

	public char getColuna() {
		return coluna;
	}

	public int getLinha() {
		return linha;
	}

	protected Posicao toPosition() {
		return new Posicao(8 - linha, coluna - 'a');
	}
	
	protected static PosicaoXadrez fromPosition(Posicao posicao) {
		System.out.println('a' - posicao.getColuna());
		return new PosicaoXadrez((char)('a' - posicao.getColuna()), 8 - posicao.getLinha() );
	}
	
	@Override
	public String toString() {
		return "" + coluna + linha;
	}
}
