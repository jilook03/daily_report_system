package actions;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.FollowView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.EmployeeService;
import services.FollowService;
import services.LikeService;
import services.ReportService;

/**
 * 日報に関する処理を行うActionクラス
 *
 */
public class ReportAction extends ActionBase {

    private ReportService service;
    private EmployeeService eService;
    private FollowService fService;
    private LikeService lService;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new ReportService();
        eService = new EmployeeService();
        fService = new FollowService();
        lService = new LikeService();

        //メソッドを実行
        invoke();
        service.close();
        eService.close();
        fService.close();
        lService.close();
    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {

        //指定されたページ数の一覧画面に表示する日報データを取得
        int page = getPage();
        List<ReportView> reports = service.getAllPerPage(page);

        //全日報データの件数を取得
        long reportsCount = service.countAll();

        putRequestScope(AttributeConst.REPORTS, reports); //取得した日報データ
        putRequestScope(AttributeConst.REP_COUNT, reportsCount); //全ての日報データの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_REP_INDEX);
    }

    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException {

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン

        //日報情報の空インスタンスに、日報の日付＝今日の日付を設定する
        ReportView rv = new ReportView();
        LocalTime begin = LocalTime.parse("09:00:00");
        LocalTime finish = LocalTime.parse("18:00:00");
        rv.setReportDate(LocalDate.now());
        rv.setBegin(begin);
        rv.setFinish(finish);
        putRequestScope(AttributeConst.REPORT, rv); //日付のみ設定済みの日報インスタンス

        //新規登録画面を表示
        forward(ForwardConst.FW_REP_NEW);

    }

    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //日報の日付が入力されていなければ、今日の日付を設定
            LocalDate day = null;
            if (getRequestParam(AttributeConst.REP_DATE) == null
                    || getRequestParam(AttributeConst.REP_DATE).equals("")) {
                day = LocalDate.now();
            } else {
                day = LocalDate.parse(getRequestParam(AttributeConst.REP_DATE));
            }

            LocalTime begin = null;
            if (!getRequestParam(AttributeConst.REP_BEGIN).equals("")) {
                begin = LocalTime.parse(getRequestParam(AttributeConst.REP_BEGIN));
            }

            LocalTime finish = null;
            if (!getRequestParam(AttributeConst.REP_FINISH).equals("")) {
                finish = LocalTime.parse(getRequestParam(AttributeConst.REP_FINISH));
            }

            //セッションからログイン中の従業員情報を取得
            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            //パラメータの値をもとに日報情報のインスタンスを作成する
            ReportView rv = new ReportView(
                    null,
                    ev, //ログインしている従業員を、日報作成者として登録する
                    day,
                    getRequestParam(AttributeConst.REP_TITLE),
                    getRequestParam(AttributeConst.REP_CONTENT),
                    null,
                    null,
                    begin,
                    finish);

            //日報情報登録
            List<String> errors = service.create(rv);

            if (errors.size() > 0) {
                //登録中にエラーがあった場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.REPORT, rv);//入力された日報情報
                putRequestScope(AttributeConst.ERR, errors);//エラーのリスト

                //新規登録画面を再表示
                forward(ForwardConst.FW_REP_NEW);

            } else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {

        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
        ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        if (rv == null) {
            //該当の日報データが存在しない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {
            boolean isLike = lService.likeCheck(ev.getId(),rv.getId());
            long likeCount = lService.likeCount(rv.getId());

            putRequestScope(AttributeConst.IS_LIKE, isLike);
            putRequestScope(AttributeConst.LIKE_COUNT, likeCount);
            putRequestScope(AttributeConst.REPORT, rv); //取得した日報データ

            //詳細画面を表示
            forward(ForwardConst.FW_REP_SHOW);
        }
    }

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

        //idを条件に日報データを取得する
        ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        if (rv == null || ev.getId() != rv.getEmployee().getId()) {
            //該当の日報データが存在しない、または
            //ログインしている従業員が日報の作成者でない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst.REPORT, rv); //取得した日報データ

            //編集画面を表示
            forward(ForwardConst.FW_REP_EDIT);
        }

    }

    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            LocalTime begin = null;
            if (!getRequestParam(AttributeConst.REP_BEGIN).equals("")) {
                begin = LocalTime.parse(getRequestParam(AttributeConst.REP_BEGIN));
            }

            LocalTime finish = null;
            if (!getRequestParam(AttributeConst.REP_FINISH).equals("")) {
                finish = LocalTime.parse(getRequestParam(AttributeConst.REP_FINISH));
            }

            //idを条件に日報データを取得する
            ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

            //入力された日報内容を設定する
            rv.setReportDate(toLocalDate(getRequestParam(AttributeConst.REP_DATE)));
            rv.setTitle(getRequestParam(AttributeConst.REP_TITLE));
            rv.setContent(getRequestParam(AttributeConst.REP_CONTENT));
            rv.setBegin(begin);
            rv.setFinish(finish);

            //日報データを更新する
            List<String> errors = service.update(rv);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.REPORT, rv); //入力された日報情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //編集画面を再表示
                forward(ForwardConst.FW_REP_EDIT);
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);

            }
        }
    }

    public void showUser() throws ServletException, IOException {

        EmployeeView login = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
        EmployeeView ev = eService.findOne(Integer.parseInt(getRequestParam(AttributeConst.EMP_ID)));

        boolean isFollow = fService.followCheck(login.getId(), ev.getId());

        int page = getPage();
        List<ReportView> reports = service.getMinePerPage(ev, page);

        long myReportsCount = service.countAllMine(ev);

        putRequestScope(AttributeConst.IS_FOLLOW, isFollow);
        putRequestScope(AttributeConst.EMPLOYEE, ev);
        putRequestScope(AttributeConst.REPORTS, reports);
        putRequestScope(AttributeConst.REP_COUNT, myReportsCount);
        putRequestScope(AttributeConst.PAGE, page);
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE);


        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        forward(ForwardConst.FW_REP_USER);
    }

    public void follow() throws ServletException, IOException {

        EmployeeView followFrom = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
        EmployeeView followTo = eService.findOne(Integer.parseInt(getRequestParam(AttributeConst.EMP_ID)));

        FollowView fv = new FollowView(
                followFrom.getId(),
                followTo.getId(),
                null,
                null);

        fService.follow(fv);

        putRequestScope(AttributeConst.FLUSH, MessageConst.I_FOLLOW.getMessage());

        showUser();

    }

    public void unFollow() throws ServletException, IOException {

        EmployeeView followFrom = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
        EmployeeView followTo = eService.findOne(Integer.parseInt(getRequestParam(AttributeConst.EMP_ID)));

        fService.unFollow(followFrom.getId(), followTo.getId());

        putRequestScope(AttributeConst.FLUSH, MessageConst.I_UNFOLLOW.getMessage());

        showUser();
    }

    public void search() throws ServletException, IOException, ParseException {
        String name = getRequestParam(AttributeConst.SEA_NAME);
        String from = getRequestParam(AttributeConst.SEA_DATE_FROM);
        String to = getRequestParam(AttributeConst.SEA_DATE_TO);
        String title = getRequestParam(AttributeConst.SEA_TITLE);

        if (!"".equals(name) && name != null || !"".equals(from) && from != null || !"".equals(to) && to != null || !"".equals(title) && title != null) {

            List<ReportView> reports = service.searchReport(name,from,to,title);
            int reportsCount = reports.size();

            putRequestScope(AttributeConst.REPORTS, reports);
            putRequestScope(AttributeConst.REP_COUNT, reportsCount);
            putRequestScope(AttributeConst.FLUSH, null);

            if (!"".equals(from) && from != null) {
                putRequestScope(AttributeConst.SEA_DATE_FROM, toDate(from));
            }
            if (!"".equals(to) && to != null) {
                putRequestScope(AttributeConst.SEA_DATE_TO, toDate(to));
            }
            if (reportsCount == 0) {
                putRequestScope(AttributeConst.FLUSH, MessageConst.E_NORESULT.getMessage());
            }
        }

        putRequestScope(AttributeConst.SEA_NAME, name);
        putRequestScope(AttributeConst.SEA_TITLE, title);

        forward(ForwardConst.FW_REP_SEARCH);
    }

    public void like() throws ServletException, IOException {
        EmployeeView loginEmp = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
        ReportView report = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));
        lService.unLike(loginEmp.getId(), report.getId());
        lService.like(loginEmp.getId(), report.getId());

        show();

    }

    public void unLike() throws ServletException, IOException {
        EmployeeView loginEmp = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
        ReportView report = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));
        lService.unLike(loginEmp.getId(), report.getId());

        show();
    }
}