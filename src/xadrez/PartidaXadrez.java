package xadrez;

import boardgame.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {
	
	private Tabuleiro tabuleiro;
	
	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		iniciarConfig();
	}
	
	public PecaXadrez[][] getPecas(){
		PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i=0; i<tabuleiro.getLinhas(); i++) {
			for (int j=0; j<tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}
	
	private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosition());
	}
	private void iniciarConfig() {
		
		colocarNovaPeca('c', 1, new Torre(tabuleiro, Cor.BRACA));
		colocarNovaPeca('c', 2, new Torre(tabuleiro, Cor.BRACA));
		colocarNovaPeca('d', 2, new Torre(tabuleiro, Cor.BRACA));
		colocarNovaPeca('e', 2, new Torre(tabuleiro, Cor.BRACA));
		colocarNovaPeca('e', 1, new Torre(tabuleiro, Cor.BRACA));
		colocarNovaPeca('d', 1, new Rei(tabuleiro, Cor.BRACA));

		colocarNovaPeca('c', 7, new Torre(tabuleiro, Cor.PRETA));
		colocarNovaPeca('c', 8, new Torre(tabuleiro, Cor.PRETA));
		colocarNovaPeca('d', 7, new Torre(tabuleiro, Cor.PRETA));
		colocarNovaPeca('e', 7, new Torre(tabuleiro, Cor.PRETA));
		colocarNovaPeca('e', 8, new Torre(tabuleiro, Cor.PRETA));
		colocarNovaPeca('d', 8, new Torre(tabuleiro, Cor.PRETA));
	}
}
