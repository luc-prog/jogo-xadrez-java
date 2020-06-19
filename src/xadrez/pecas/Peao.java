package xadrez.pecas;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;

public class Peao extends PecaXadrez{

	private PartidaXadrez partidaXadrez;
	
	public Peao(Tabuleiro tabuleiro, Cor cor, PartidaXadrez partidaXadrez) {
		super(tabuleiro, cor);
		this.partidaXadrez = partidaXadrez;
	}
	
	@Override
	public String toString() {
		return "P";
	}
	
	@Override
	public boolean[][] possiveisMovimentos() {
		
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		Posicao p = new Posicao(0, 0);
		
		if(getCor() == Cor.BRANCA) {
			p.setValues(posicao.getLinha() - 1, posicao.getColuna());
			if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() - 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
			if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p) && getTabuleiro().posicaoExiste(p2) && !getTabuleiro().existePeca(p2) && getContarMovimento() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() - 1, posicao.getColuna() - 1);
			if(getTabuleiro().posicaoExiste(p) && existePecaOponente(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() - 1, posicao.getColuna() + 1);
			if(getTabuleiro().posicaoExiste(p) && existePecaOponente(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			
			// #specialmove en passant white
			if (posicao.getLinha() == 3) {
				Posicao left = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().posicaoExiste(left) && existePecaOponente(left) && getTabuleiro().peca(left) == partidaXadrez.getEnPassantVulnerable()) {
					mat[left.getLinha() - 1][left.getColuna()] = true;
				}
				Posicao right = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				if (getTabuleiro().posicaoExiste(right) && existePecaOponente(right) && getTabuleiro().peca(right) == partidaXadrez.getEnPassantVulnerable()) {
					mat[right.getLinha() - 1][right.getColuna()] = true;
				}
			}
		}
		else {
			p.setValues(posicao.getLinha() + 1, posicao.getColuna());
			if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() + 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
			if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().existePeca(p) && getTabuleiro().posicaoExiste(p2) && !getTabuleiro().existePeca(p2) && getContarMovimento() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() + 1, posicao.getColuna() + 1);
			if(getTabuleiro().posicaoExiste(p) && existePecaOponente(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() + 1, posicao.getColuna() - 1);
			if(getTabuleiro().posicaoExiste(p) && existePecaOponente(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			// #specialmove en passant white
			if (posicao.getLinha() == 4) {
				Posicao left = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().posicaoExiste(left) && existePecaOponente(left) && getTabuleiro().peca(left) == partidaXadrez.getEnPassantVulnerable()) {
					mat[left.getLinha() - 1][left.getColuna()] = true;
				}
				Posicao right = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				if (getTabuleiro().posicaoExiste(right) && existePecaOponente(right)
						&& getTabuleiro().peca(right) == partidaXadrez.getEnPassantVulnerable()) {
					mat[right.getLinha() + 1][right.getColuna()] = true;
				}
			}
		}
		return mat;
	}
}
