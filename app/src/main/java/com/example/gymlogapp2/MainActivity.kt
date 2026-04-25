package com.example.gymlogapp2

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.gymlogapp2.ui.theme.GymLogApp2Theme
import org.json.JSONArray
import org.json.JSONObject

data class GymRecord(
    val id: Int,
    val exerciseName: String,
    val weight: String,
    val reps: String
)

data class ExerciseInfo(
    val name: String,
    val category: String,
    val afterCare: String,
    val mealAdvice: String
)

val exerciseList = listOf(
    ExerciseInfo(
        "ベンチプレス",
        "胸",
        "胸、肩前部、上腕三頭筋を軽く伸ばす。肩に違和感がある日は無理に可動域を広げない。",
        "トレ後はタンパク質と炭水化物を両方取る。鶏肉、卵、米、バナナなどがおすすめ。"
    ),
    ExerciseInfo(
        "インクラインベンチプレス",
        "胸",
        "胸上部と肩前部を重点的にケア。肩が詰まる感覚がある場合は軽めのストレッチにする。",
        "胸上部を狙う日はタンパク質をしっかり。卵、魚、鶏肉、米などが向いている。"
    ),
    ExerciseInfo(
        "ダンベルプレス",
        "胸",
        "胸と肩周りを左右差なく伸ばす。深く下ろしすぎた日は肩前部を丁寧にケア。",
        "筋肉修復用にタンパク質を優先。炭水化物も削りすぎない。"
    ),
    ExerciseInfo(
        "インクラインダンベルプレス",
        "胸",
        "胸上部と肩前部を軽く伸ばす。肩に違和感がある場合は重量を落とす。",
        "タンパク質と炭水化物を両方取る。米、卵、鶏肉、魚など。"
    ),
    ExerciseInfo(
        "ダンベルフライ",
        "胸",
        "胸を開くストレッチを軽く行う。やりすぎると肩に負担が出るので反動は避ける。",
        "軽めの日でもタンパク質は確保。ヨーグルト、卵、鶏肉など。"
    ),
    ExerciseInfo(
        "ケーブルクロスオーバー",
        "胸",
        "胸の中央から外側をゆっくり伸ばす。肩をすくめないように意識する。",
        "高回数で追い込んだ日は糖質も少し入れる。米や果物が使いやすい。"
    ),
    ExerciseInfo(
        "ペックフライ",
        "胸",
        "胸の外側から中央にかけて軽く伸ばす。肩前部に痛みがあれば休む。",
        "タンパク質中心。鶏肉、卵、魚、豆腐など。"
    ),
    ExerciseInfo(
        "ディップス",
        "胸",
        "胸下部、肩前部、上腕三頭筋をケア。肩に痛みがある場合は無理しない。",
        "自重でも消耗は大きい。タンパク質と水分をしっかり取る。"
    ),
    ExerciseInfo(
        "プッシュアップ",
        "胸",
        "胸、肩、腕を軽く伸ばす。手首に違和感がある時は手首もほぐす。",
        "軽めなら通常食でOK。タンパク質は欠かさない。"
    ),

    ExerciseInfo(
        "ラットプルダウン",
        "背中",
        "広背筋、肩甲骨周り、上腕二頭筋を軽く伸ばす。首をすくめる癖がある場合は僧帽筋もほぐす。",
        "背中の日はタンパク質を多めに。鶏肉、魚、豆腐、米が無難。"
    ),
    ExerciseInfo(
        "チンニング",
        "背中",
        "広背筋と腕をケア。肩甲骨周りが固まりやすいので軽く回す。",
        "高負荷なのでタンパク質と炭水化物を両方。米、肉、卵が合う。"
    ),
    ExerciseInfo(
        "ベントオーバーロウ",
        "背中",
        "背中全体と腰周りをケア。腰に張りがある場合は無理に反らさない。",
        "背中と腰を使うので、食事はしっかり。肉、魚、米、味噌汁など。"
    ),
    ExerciseInfo(
        "ワンハンドダンベルロウ",
        "背中",
        "広背筋と肩甲骨周りを左右それぞれ伸ばす。腰を丸めないように注意。",
        "タンパク質中心。片側種目で疲れが偏った日は水分補給も意識。"
    ),
    ExerciseInfo(
        "シーテッドロー",
        "背中",
        "肩甲骨を寄せる動きで固まった背中を軽くほぐす。胸を開くストレッチも有効。",
        "中重量の日はバランス重視。米、卵、魚、野菜など。"
    ),
    ExerciseInfo(
        "Tバーロウ",
        "背中",
        "背中中央、広背筋、腰周りをケア。腰が張る場合は無理に反らさない。",
        "高重量なら米とタンパク質をしっかり。肉、魚、卵など。"
    ),
    ExerciseInfo(
        "ケーブルロー",
        "背中",
        "肩甲骨周りと広背筋を軽く伸ばす。背中を丸めすぎない。",
        "通常のタンパク質補給でOK。"
    ),
    ExerciseInfo(
        "フェイスプル",
        "背中",
        "肩後部、肩甲骨周り、僧帽筋をほぐす。首をすくめない。",
        "補助種目なので通常食でOK。肩周りの疲労が強い日は休養。"
    ),
    ExerciseInfo(
        "デッドリフト",
        "背中",
        "背中、臀部、ハムストリングスを重点的にケア。腰に違和感がある時は休養優先。",
        "消耗が大きいので炭水化物を削りすぎない。米、肉、卵、バナナなど。"
    ),
    ExerciseInfo(
        "バックエクステンション",
        "背中",
        "腰背部と臀部を軽く伸ばす。痛みがある場合はストレッチより休養を優先。",
        "軽めなら通常食でOK。タンパク質は欠かさない。"
    ),

    ExerciseInfo(
        "スクワット",
        "脚",
        "大腿四頭筋、ハムストリングス、臀部、股関節を丁寧にケア。翌日は軽い散歩も有効。",
        "脚トレ後は消耗が大きいので米や芋などの炭水化物も取る。タンパク質も多めに。"
    ),
    ExerciseInfo(
        "フロントスクワット",
        "脚",
        "大腿四頭筋、股関節、体幹をケア。手首や肩が疲れる場合は上半身も軽く伸ばす。",
        "高負荷なら米と肉をしっかり。消耗を戻す食事を意識する。"
    ),
    ExerciseInfo(
        "レッグプレス",
        "脚",
        "太もも全体と臀部を伸ばす。膝に違和感がある日は深く曲げすぎない。",
        "脚の日なので炭水化物も重要。米、鶏肉、卵、味噌汁など。"
    ),
    ExerciseInfo(
        "ブルガリアンスクワット",
        "脚",
        "臀部、太もも前、股関節を片側ずつケア。翌日に強い筋肉痛が出やすい。",
        "片脚種目は負荷が高い。タンパク質と水分を多めに。"
    ),
    ExerciseInfo(
        "ランジ",
        "脚",
        "股関節、臀部、大腿四頭筋をケア。左右差が出やすいので両脚を確認する。",
        "軽食ならバナナやプロテイン、本食なら米と肉が無難。"
    ),
    ExerciseInfo(
        "レッグエクステンション",
        "脚",
        "大腿四頭筋を中心に軽く伸ばす。膝周りに痛みがある時は無理しない。",
        "単関節種目なので通常のタンパク質補給で十分。"
    ),
    ExerciseInfo(
        "レッグカール",
        "脚",
        "ハムストリングスを丁寧に伸ばす。急に強く伸ばすとつりやすいので注意。",
        "ハムを追い込んだ日は水分とミネラルも意識する。"
    ),
    ExerciseInfo(
        "ルーマニアンデッドリフト",
        "脚",
        "ハムストリングス、臀部、腰背部を重点的にケア。腰の張りが強い日は休む。",
        "高負荷なら炭水化物も取る。米、肉、卵、魚など。"
    ),
    ExerciseInfo(
        "ヒップスラスト",
        "脚",
        "臀部と股関節前側をケア。腰を反りすぎた場合は腰回りも軽くほぐす。",
        "臀部狙いでも高負荷ならタンパク質と糖質を両方取る。"
    ),
    ExerciseInfo(
        "カーフレイズ",
        "脚",
        "ふくらはぎをゆっくり伸ばす。つりやすい人は水分とミネラルも意識。",
        "通常食でOK。汗をかいた日は水分補給を忘れない。"
    ),

    ExerciseInfo(
        "ショルダープレス",
        "肩",
        "肩前部、側部、上腕三頭筋を軽くケア。首や僧帽筋が固まりやすい。",
        "肩の日はタンパク質中心。鶏肉、卵、魚、豆腐など。"
    ),
    ExerciseInfo(
        "ダンベルショルダープレス",
        "肩",
        "肩全体と上腕三頭筋をケア。肩関節に違和感がある場合は軽めにする。",
        "タンパク質と水分を確保。高重量なら炭水化物も少し取る。"
    ),
    ExerciseInfo(
        "サイドレイズ",
        "肩",
        "肩側部を軽く回す。首をすくめていた場合は僧帽筋もほぐす。",
        "軽めの種目でもタンパク質は確保。食事は通常でOK。"
    ),
    ExerciseInfo(
        "フロントレイズ",
        "肩",
        "肩前部と胸上部を軽く伸ばす。肩に痛みがある場合は休む。",
        "タンパク質を意識。高回数なら軽く糖質も取る。"
    ),
    ExerciseInfo(
        "リアレイズ",
        "肩",
        "肩後部と肩甲骨周りをケア。猫背気味なら胸を開くストレッチも有効。",
        "肩後部は小さい筋肉なので、通常のタンパク質補給で十分。"
    ),
    ExerciseInfo(
        "アーノルドプレス",
        "肩",
        "肩全体と肩甲骨周りをケア。ひねり動作があるので違和感があれば軽めにする。",
        "高負荷なら炭水化物も少し。卵、米、魚など。"
    ),
    ExerciseInfo(
        "アップライトロウ",
        "肩",
        "肩側部と僧帽筋をケア。肩に詰まりが出る場合は可動域を浅くする。",
        "通常のタンパク質補給でOK。"
    ),
    ExerciseInfo(
        "シュラッグ",
        "肩",
        "僧帽筋と首周りをほぐす。首を強く回しすぎない。",
        "通常食でOK。肩こり感が強いなら水分も意識。"
    ),

    ExerciseInfo(
        "バーベルカール",
        "腕",
        "上腕二頭筋と前腕を軽く伸ばす。肘に違和感がある時は無理しない。",
        "腕の日でもタンパク質は重要。卵、魚、鶏肉など。"
    ),
    ExerciseInfo(
        "ダンベルカール",
        "腕",
        "上腕二頭筋を左右それぞれ軽くケア。前腕が張る場合は手首周りもほぐす。",
        "通常のタンパク質補給でOK。"
    ),
    ExerciseInfo(
        "ハンマーカール",
        "腕",
        "前腕と上腕筋をケア。握力が疲れやすいので手首を軽くほぐす。",
        "タンパク質中心。水分補給も忘れない。"
    ),
    ExerciseInfo(
        "プリーチャーカール",
        "腕",
        "上腕二頭筋を軽く伸ばす。肘周りに負担が出やすいので重量を欲張らない。",
        "通常のタンパク質補給でOK。"
    ),
    ExerciseInfo(
        "ケーブルカール",
        "腕",
        "二頭筋と前腕を軽くケア。高回数でパンプした日は水分も取る。",
        "タンパク質中心。軽食ならプロテインや卵でもよい。"
    ),
    ExerciseInfo(
        "トライセプスプレスダウン",
        "腕",
        "上腕三頭筋を伸ばす。肘に痛みがある場合は重量を下げる。",
        "腕トレ後はタンパク質を確保。鶏肉、卵、豆腐など。"
    ),
    ExerciseInfo(
        "スカルクラッシャー",
        "腕",
        "上腕三頭筋と肘周りをケア。肘への負担が出やすいので無理しない。",
        "タンパク質中心。関節に違和感がある日は休養も大事。"
    ),
    ExerciseInfo(
        "フレンチプレス",
        "腕",
        "上腕三頭筋の長頭を軽く伸ばす。肩や肘に痛みがあれば控える。",
        "タンパク質を取る。水分不足だと肘周りが張りやすい。"
    ),
    ExerciseInfo(
        "ナローベンチプレス",
        "腕",
        "上腕三頭筋、胸、肩前部をケア。肩と肘の違和感に注意。",
        "高重量なら炭水化物も取る。米、肉、卵が無難。"
    ),
    ExerciseInfo(
        "キックバック",
        "腕",
        "上腕三頭筋を軽く伸ばす。軽重量で丁寧に行った日は過度なケアは不要。",
        "通常食でOK。タンパク質は確保する。"
    ),

    ExerciseInfo(
        "クランチ",
        "体幹",
        "腹直筋を軽く伸ばす。腰を反らしすぎないように注意。",
        "軽めなら通常食でOK。全身トレ後ならタンパク質を取る。"
    ),
    ExerciseInfo(
        "レッグレイズ",
        "体幹",
        "腹部と股関節周りをケア。腰に違和感がある場合はフォームを見直す。",
        "通常食でOK。腹筋だけの日でも水分は取る。"
    ),
    ExerciseInfo(
        "プランク",
        "体幹",
        "腹部、肩、背中を軽くほぐす。腰が反った感覚があるなら休む。",
        "通常食でOK。長時間行った日はタンパク質も意識。"
    ),
    ExerciseInfo(
        "サイドプランク",
        "体幹",
        "腹斜筋と肩周りを軽くケア。肩が痛い場合は時間を短くする。",
        "通常食でOK。"
    ),
    ExerciseInfo(
        "アブローラー",
        "体幹",
        "腹部と広背筋、肩周りをケア。腰に痛みがある場合は無理しない。",
        "負荷が高いのでタンパク質を取る。全身疲労があれば炭水化物も。"
    ),
    ExerciseInfo(
        "ケーブルクランチ",
        "体幹",
        "腹部と腰周りを軽くほぐす。反動を使った日は腰の違和感に注意。",
        "通常食でOK。"
    ),
    ExerciseInfo(
        "ロシアンツイスト",
        "体幹",
        "腹斜筋と腰周りを軽くほぐす。勢いでひねりすぎない。",
        "通常食でOK。"
    ),
    ExerciseInfo(
        "ハンギングレッグレイズ",
        "体幹",
        "腹部、股関節、広背筋をケア。握力疲労が強い場合は前腕もほぐす。",
        "負荷が高めなのでタンパク質を意識。"
    ),

    ExerciseInfo(
        "グッドモーニング",
        "補助",
        "ハムストリングス、臀部、腰背部を丁寧にケア。腰に違和感があれば休養優先。",
        "高負荷なら炭水化物とタンパク質をしっかり。"
    ),
    ExerciseInfo(
        "ザーチャースクワット",
        "補助",
        "脚、体幹、腕の保持部分をケア。肘や前腕に負担が出やすい。",
        "全身負荷が高いので、米と肉などしっかりした食事が向いている。"
    ),
    ExerciseInfo(
        "スモウデッドリフト",
        "補助",
        "内もも、臀部、ハム、腰背部をケア。股関節周りを丁寧にほぐす。",
        "高重量なら炭水化物も多めに。米、肉、卵、魚など。"
    ),
    ExerciseInfo(
        "リバースランジ",
        "補助",
        "臀部、太もも前、股関節をケア。膝に不安がある場合は歩幅を調整。",
        "脚種目なので米や芋などの炭水化物も取る。"
    ),
    ExerciseInfo(
        "ステップアップ",
        "補助",
        "臀部、大腿四頭筋、ふくらはぎをケア。片脚ずつ疲労を確認する。",
        "軽中負荷なら通常食でOK。タンパク質は確保。"
    ),
    ExerciseInfo(
        "ゴブレットスクワット",
        "補助",
        "大腿四頭筋、臀部、股関節をケア。初心者でも丁寧にストレッチしやすい。",
        "軽中負荷なら通常食でOK。タンパク質は確保。"
    ),
    ExerciseInfo(
        "ランドマインプレス",
        "補助",
        "肩前部、胸上部、体幹をケア。肩に違和感がある場合は軽めに。",
        "タンパク質中心。高重量なら炭水化物も少し。"
    ),
    ExerciseInfo(
        "ランドマインロウ",
        "補助",
        "背中、肩甲骨周り、腰をケア。腰が丸まっていた場合は休養も意識。",
        "背中の日としてタンパク質を多めに。"
    ),
    ExerciseInfo(
        "パロフプレス",
        "補助",
        "腹斜筋と体幹を軽くほぐす。強い筋肉痛は出にくいが姿勢を整える。",
        "通常食でOK。"
    ),
    ExerciseInfo(
        "ファーマーズウォーク",
        "補助",
        "前腕、僧帽筋、体幹、脚をケア。握力疲労が強い場合は手首もほぐす。",
        "全身種目なので水分とタンパク質を取る。長距離なら炭水化物も。"
    ),
    ExerciseInfo(
        "ケトルベルスイング",
        "補助",
        "ハム、臀部、腰背部、肩周りをケア。腰で振らず股関節を使う意識。",
        "全身運動なのでタンパク質と炭水化物を両方。"
    ),
    ExerciseInfo(
        "スレッドプッシュ",
        "補助",
        "脚全体、臀部、ふくらはぎをケア。息が上がりやすいのでクールダウンを入れる。",
        "消耗が大きいので炭水化物と水分をしっかり取る。"
    ),
    ExerciseInfo(
        "シシースクワット",
        "補助",
        "大腿四頭筋を丁寧に伸ばす。膝に不安がある場合は無理しない。",
        "単関節寄りだが負荷は強い。タンパク質を意識。"
    ),
    ExerciseInfo(
        "ノルディックハムカール",
        "補助",
        "ハムストリングスを特に丁寧にケア。強い筋肉痛が出やすいので翌日は無理しない。",
        "ハムへの負荷が大きい。タンパク質、水分、炭水化物をしっかり。"
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GymLogApp2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GymLogScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GymLogScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val savedRecords = remember { loadRecords(context) }
    val records = remember {
        mutableStateListOf<GymRecord>().apply {
            addAll(savedRecords)
        }
    }

    var nextId by remember {
        mutableStateOf(
            if (savedRecords.isEmpty()) 1 else savedRecords.maxOf { it.id } + 1
        )
    }

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("記録入力", "記録一覧")

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> WorkoutInputScreen(
                records = records,
                nextId = nextId,
                onNextIdChange = { nextId = it },
                onSave = {
                    saveRecords(context, records)
                },
                modifier = Modifier.fillMaxSize()
            )

            1 -> RecordListScreen(
                records = records,
                onDelete = { record ->
                    records.remove(record)
                    saveRecords(context, records)
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun WorkoutInputScreen(
    records: MutableList<GymRecord>,
    nextId: Int,
    onNextIdChange: (Int) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("胸") }
    var selectedExercise by remember { mutableStateOf(exerciseList.first()) }

    var exerciseName by remember { mutableStateOf(selectedExercise.name) }
    var weight by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }

    val categories = exerciseList.map { it.category }.distinct()
    val filteredExercises = exerciseList.filter { it.category == selectedCategory }

    LazyColumn(
        modifier = modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Gym Log",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "種目を選んで、記録とケアをまとめるアプリ",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Text(
                text = "部位カテゴリ",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    AssistChip(
                        onClick = {
                            selectedCategory = category
                            val firstExercise = exerciseList.first { it.category == category }
                            selectedExercise = firstExercise
                            exerciseName = firstExercise.name
                        },
                        label = { Text(category) }
                    )
                }
            }
        }

        item {
            Text(
                text = "種目",
                style = MaterialTheme.typography.titleMedium
            )
        }

        items(filteredExercises) { exercise ->
            OutlinedButton(
                onClick = {
                    selectedExercise = exercise
                    exerciseName = exercise.name
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(exercise.name)
            }
        }

        item {
            AdviceCard(selectedExercise)
        }

        item {
            OutlinedTextField(
                value = exerciseName,
                onValueChange = { exerciseName = it },
                label = { Text("種目名") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("重量 kg") },
                    placeholder = { Text("60") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                OutlinedTextField(
                    value = reps,
                    onValueChange = { reps = it },
                    label = { Text("回数") },
                    placeholder = { Text("10") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Button(
                onClick = {
                    if (exerciseName.isNotBlank() && weight.isNotBlank() && reps.isNotBlank()) {
                        val newRecord = GymRecord(
                            id = nextId,
                            exerciseName = exerciseName,
                            weight = weight,
                            reps = reps
                        )

                        records.add(newRecord)
                        onSave()

                        onNextIdChange(nextId + 1)

                        weight = ""
                        reps = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("記録を追加")
            }
        }
    }
}

@Composable
fun RecordListScreen(
    records: List<GymRecord>,
    onDelete: (GymRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(20.dp)
    ) {
        Text(
            text = "記録一覧",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "追加したトレーニング記録を確認できます",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (records.isEmpty()) {
            Text("まだ記録はありません")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(records.reversed(), key = { it.id }) { record ->
                    RecordCard(
                        record = record,
                        onDelete = {
                            onDelete(record)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AdviceCard(exercise: ExerciseInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "おすすめアフターケア",
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = exercise.afterCare,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "おすすめ食事",
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = exercise.mealAdvice,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun RecordCard(
    record: GymRecord,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = record.exerciseName,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${record.weight}kg × ${record.reps}回",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("削除")
            }
        }
    }
}

fun saveRecords(context: Context, records: List<GymRecord>) {
    val jsonArray = JSONArray()

    records.forEach { record ->
        val jsonObject = JSONObject().apply {
            put("id", record.id)
            put("exerciseName", record.exerciseName)
            put("weight", record.weight)
            put("reps", record.reps)
        }

        jsonArray.put(jsonObject)
    }

    context
        .getSharedPreferences("gym_log_preferences", Context.MODE_PRIVATE)
        .edit()
        .putString("records", jsonArray.toString())
        .apply()
}

fun loadRecords(context: Context): List<GymRecord> {
    val jsonText = context
        .getSharedPreferences("gym_log_preferences", Context.MODE_PRIVATE)
        .getString("records", null)
        ?: return emptyList()

    return try {
        val jsonArray = JSONArray(jsonText)
        val records = mutableListOf<GymRecord>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)

            records.add(
                GymRecord(
                    id = jsonObject.getInt("id"),
                    exerciseName = jsonObject.getString("exerciseName"),
                    weight = jsonObject.getString("weight"),
                    reps = jsonObject.getString("reps")
                )
            )
        }

        records
    } catch (error: Exception) {
        emptyList()
    }
}