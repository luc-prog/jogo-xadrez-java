package xadrez.pecas;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Bispo extends PecaXadrez{

	public Bispo(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
		
	}
	
	@Override
	public String toString() {
		return "B";
	}
	
	@Override
	public boolean[][] possiveisMovimentos() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		
		Posicao p = new Posicao(0, 0);
		//nw
		p.setValues(posicao.getLinha() - 1, posicao.getColuna() - 1);
		while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() - 1, p.getColuna() - 1);
		}
		if (getTabuleiro().posicaoExiste(p) && existePecaOponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		//ne
		p.setValues(posicao.getLinha() - 1, posicao.getColuna() + 1);
		while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() - 1, p.getColuna() + 1);
		}
		if (getTabuleiro().posicaoExiste(p) && existePecaOponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		//se
		p.setValues(posicao.getLinha() + 1, posicao.getColuna() + 1);
		while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() + 1, p.getColuna() + 1);
		}
		if (getTabuleiro().posicaoExiste(p) && existePecaOponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		//sw
		p.setValues(posicao.getLinha() + 1, posicao.getColuna() - 1);
		while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() + 1, p.getColuna() - 1);
		}
		if (getTabuleiro().posicaoExiste(p) && existePecaOponente(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		return mat;
	}
}
