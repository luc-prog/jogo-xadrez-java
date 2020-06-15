package xadrez;


import boardgame.Peca;
import boardgame.Posicao;
import boardgame.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {
	
	public int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	
	
	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cor.BRACA;
		iniciarConfig();
	}
	
	public int getTurno() {
		return turno;
	}
	
	public Cor getJogadorAtual() {
		return jogadorAtual;
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
	
	public boolean[][] possivelMovimentos(PosicaoXadrez pInicial){
		Posicao posicao = pInicial.toPosition();
		validarPosicInicial(posicao);
		return tabuleiro.peca(posicao).possiveisMovimentos();
	}
	public PecaXadrez movimentoXadrez(PosicaoXadrez posicInicial, PosicaoXadrez posicFinal) {
		Posicao pInicial = posicInicial.toPosition();
		Posicao pFinal = posicFinal.toPosition();
		validarPosicInicial(pInicial);
		validarPosicFinal(pInicial, pFinal);
		Peca pecaCapturada = fazerMover(pInicial, pFinal);
		proximoTurno();
		return (PecaXadrez)pecaCapturada;
	}
	
	private Peca fazerMover(Posicao inicial, Posicao pFinal) {
		Peca p = tabuleiro.removerPeca(inicial);
		Peca pecaCapturada = tabuleiro.removerPeca(pFinal);
		tabuleiro.colocarPeca(p, pFinal);
		return pecaCapturada;
		
	}
	
	private void validarPosicInicial(Posicao posicao) {
		if (!tabuleiro.existePeca(posicao)) {
			throw new ExcecaoXadrez("peça não existe no tabuleiro");
		}
		if (jogadorAtual != ((PecaXadrez)tabuleiro.peca(posicao)).getCor()) {
			throw new ExcecaoXadrez("A peça escolhida não é sua");
		}
		if (!tabuleiro.peca(posicao).existeMovimento()) {
			throw new ExcecaoXadrez("Não existe movimento possivel para a peça escolhida");
		}
	}
	
	private void validarPosicFinal(Posicao pInicial, Posicao pFinal) {
		if (!tabuleiro.peca(pInicial).possivelMovimento(pFinal)) {
			throw new ExcecaoXadrez("Peça escolhida nao pode mover para a posição de destino");
		}
	}
	
	private void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Cor.BRACA) ? Cor.PRETA : Cor.BRACA;
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
		colocarNovaPeca('d', 8, new Rei(tabuleiro, Cor.PRETA));
	}
}
