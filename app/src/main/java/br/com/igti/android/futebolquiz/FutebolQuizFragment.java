package br.com.igti.android.futebolquiz;

import android.animation.Animator;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FutebolQuizFragment extends Fragment {

    private static final String TAG = "FutebolQuizActivity";
    private static final String PREF_PRIMEIRA_VEZ = "primeiraVez";

    private static final String KEY_INDICE = "indice";

    private Button mBotaoVerdade;
    private Button mBotaoFalso;
    private TextView mConteudoCard;
    private CardView mCardView;

    private int mIndiceAtual = 0;
    private AudioPlayer aPlayer;

    private Pergunta[] mPerguntas = new Pergunta[]{
            new Pergunta(R.string.cardview_conteudo_joinville,true),
            new Pergunta(R.string.cardview_conteudo_cruzeiro,false),
            new Pergunta(R.string.cardview_conteudo_gremio,false)
    };

    @Override
    public void onResume() {
        super.onResume();
        boolean primeiraVez = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean(PREF_PRIMEIRA_VEZ,true);

        if (primeiraVez) {
            PreferenceManager.getDefaultSharedPreferences(getContext())
                    .edit()
                    .putBoolean(PREF_PRIMEIRA_VEZ,false)
                    .commit();
            Toast.makeText(getContext(), "Bem-vindo ao FutebolQuiz!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_futebol_quiz, container, false);

        mBotaoVerdade = (Button)v.findViewById(R.id.botaoVerdade);
        mBotaoVerdade.setOnClickListener(mBotaoVerdadeListener);
        mBotaoFalso = (Button)v.findViewById(R.id.botaoFalso);
        mBotaoFalso.setOnClickListener(mBotaoFalsoListener);

        mConteudoCard = (TextView)v.findViewById(R.id.cardviewConteudo);
        atualizaQuestao();

        mCardView = (CardView)v.findViewById(R.id.cardview);
        this.aPlayer = new AudioPlayer();

        return v;
    }

    private void atualizaQuestao() {
        int questao = mPerguntas[mIndiceAtual].getQuestao();
        mConteudoCard.setText(questao);
    }

    private void revelaCard() {
        //criando um reveal circular
        Animator animator = ViewAnimationUtils.createCircularReveal(
                mCardView,
                0,
                0,
                0,
                (float) Math.hypot(mCardView.getWidth(), mCardView.getHeight()));

        // interpolador ease-in/ease-out.
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    private void checaResposta(boolean botaoPressionado) {
        boolean resposta = mPerguntas[mIndiceAtual].isQuestaoVerdadeira();

        int recursoRespostaId = 0;

        if (botaoPressionado == resposta) {
            recursoRespostaId = R.string.toast_acertou;
            aPlayer.play(getContext(), R.raw.cashregister);
        } else {
            recursoRespostaId = R.string.toast_errou;
            aPlayer.play(getContext(), R.raw.buzzer);
        }

        Toast.makeText(getContext(), recursoRespostaId, Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener mBotaoVerdadeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checaResposta(true);
            mIndiceAtual = (mIndiceAtual + 1) % mPerguntas.length;
            atualizaQuestao();
            revelaCard();
        }
    };

    private View.OnClickListener mBotaoFalsoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checaResposta(false);
            mIndiceAtual = (mIndiceAtual + 1) % mPerguntas.length;
            atualizaQuestao();
            revelaCard();
        }
    };
}