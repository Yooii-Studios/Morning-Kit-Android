package com.yooiistudios.morningkit.panel.worldclock.model;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MNTimeZoneSearchAsyncTask extends AsyncTask<String, Object, ArrayList<MNTimeZone>> {
	public static final String TAG = "WorldClockTimeZoneSearchThread";

//	private SearchTimezoneAdapter m_Adapter = null;
	private ArrayList<MNTimeZone> allTimeZones = null;
	private Context context;
    private OnTimeZoneSearchAsyncTaskListener listener;
	
	static Comparator<MNTimeZone> comparator = null;

    public interface OnTimeZoneSearchAsyncTaskListener {
        public void onTimeZoneSearchFinished(ArrayList<MNTimeZone> filteredTimeZones);
    }

	public MNTimeZoneSearchAsyncTask(ArrayList<MNTimeZone> allTimeZones,
                                     OnTimeZoneSearchAsyncTaskListener listener, Context context) {
		this.allTimeZones = allTimeZones;
		this.listener = listener;
		this.context = context;
		
		if (comparator == null) {
			comparator = new Comparator<MNTimeZone>() {

				@Override
				public int compare(MNTimeZone lhs, MNTimeZone rhs) {
					return lhs.getPriority() - rhs.getPriority();
				}
			};
		}
	}

	@Override
	protected ArrayList<MNTimeZone> doInBackground(String... params) {

		if (params.length > 0) {
			CharSequence s = params[0];

			String searchingString = s.toString().toLowerCase();

			// search city
			if (!searchingString.isEmpty()) {

				ArrayList<MNTimeZone> sameTimeZoneStartWith = new ArrayList<MNTimeZone>();
				ArrayList<MNTimeZone> sameTimeZoneContains = new ArrayList<MNTimeZone>();
//				String previousCountry = ""; 

                for (MNTimeZone currentTimeZone : allTimeZones) {
                    if (isCancelled())
                        break;

                    // 이 부분은 뭐하는 곳인지 모르겠어서 일단 주석 처리. 기존에
                    // 여기서 계속 프로세스가 종료되고 있었음.
                    /*
                    // country changed
                    if (previousCountry.compareToIgnoreCase(currentTimeZone
                            .getName()) != 0) {
                        ArrayList<TimeZoneCity> tempBuffer = (ArrayList<TimeZoneCity>) sameTimeZoneStartWith
                                .clone();
//							tempBuffer.addAll(sameTimeZoneContains);
                        publishProgress(tempBuffer);

                        sameTimeZoneStartWith.clear();
                        sameTimeZoneContains.clear();

                        previousCountry = new String(
                                currentTimeZone.getName());
                    }
                    */

                    // 다국어 검색 알고리즘을 실행하기 전 기존 검색 먼저 실시
                    // 소문자를 기본으로. 띄어쓰기 없는 경우도 포함
                    boolean isEnglishNameFound = false;

                    // first, find strings start with the searchingString
                    String compareString = currentTimeZone.getName().toLowerCase();
                    String compareStringWitNoBlank = currentTimeZone.getName()
                            .toLowerCase().replaceAll(" ", "");

                    if (!isEnglishNameFound) {
//							Log.i(TAG, "English Search");
                        if (compareString.startsWith(searchingString)
                                || compareStringWitNoBlank
                                .startsWith(searchingString)) {
//								if (searchingString.equals("shan")) {
//									Log.i(TAG, "English search startsWith / name: " + currentTimeZone.getName() + "/priority: " + currentTimeZone.getPriority());
//								} 
                            sameTimeZoneStartWith.add(currentTimeZone);
//								currentTimeZone.setPriority(-1000 + i);
//								Log.i(TAG, "English search startsWith / name: " + currentTimeZone.getName() + "/priority: " + currentTimeZone.getPriority());
                            isEnglishNameFound = true;
                        } else if (compareString.contains(searchingString)) {
                            sameTimeZoneContains.add(currentTimeZone);
//								currentTimeZone.setPriority(currentTimeZone.getPriority() + 1000);
//								Log.i(TAG, "English search contains / name: " + currentTimeZone.getName() + "/priority: " + currentTimeZone.getPriority());
                            isEnglishNameFound = true;
                        }
                    }

                    // 다국어 검색 알고리즘 - for 문을 돌리는데 하나만 검색이 되어야 한다.
                    boolean isLocalizedNameFound = false;
                    currentTimeZone.setSearchedLocalizedName(null);
                    if (!isEnglishNameFound) {
                        ArrayList<String> localizedNames = currentTimeZone.getLocalizedNames();
                        if (localizedNames != null) {
//								Log.i(TAG, "Localize String Search");

                            // 다국어는 시작하는 것 먼저 검색하고, 포함하는 것 다시 검색하기
                            for (String localizedCampareString : localizedNames) {
                                if (currentTimeZone.getSearchedLocalizedName() == null) {
                                    if (localizedCampareString.startsWith(searchingString)) {
                                        sameTimeZoneStartWith.add(currentTimeZone);
//											currentTimeZone.setPriority(-1000 + i);
//											currentTimeZone.setName(localizedCampareString);
                                        currentTimeZone.setSearchedLocalizedName(localizedCampareString);
                                        isLocalizedNameFound = true;
                                        break;
                                    }
                                } else {
                                    break;
                                }
                            }

                            for (String localizedCampareString : localizedNames) {
                                if (currentTimeZone.getSearchedLocalizedName() == null) {
                                    if (localizedCampareString.contains(searchingString)) {
                                        sameTimeZoneContains.add(currentTimeZone);
//											currentTimeZone.setPriority(currentTimeZone.getPriority() + 1000);
//											currentTimeZone.setPriority(i);
//											currentTimeZone.setName(localizedCampareString);
                                        currentTimeZone.setSearchedLocalizedName(localizedCampareString);
                                        isLocalizedNameFound = true;
                                        break;
                                    }
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                    // TimeZone 도 검색 대상에 포함 - 영어, 다국어 모두 검색이 되지 않았을 경우만
                    if (!isEnglishNameFound && !isLocalizedNameFound) {
                        String compareTimeZoneString = currentTimeZone.getTimeZoneName().toLowerCase();
                        String compareTimeZoneStringWitNoBlank = currentTimeZone.getTimeZoneName()
                                .toLowerCase().replaceAll(" ", "");
                        if (compareTimeZoneString.startsWith(searchingString)
                                || compareTimeZoneStringWitNoBlank
                                .startsWith(searchingString)) {
                            sameTimeZoneStartWith.add(currentTimeZone);
                        } else if (compareString.contains(searchingString)) {
                            sameTimeZoneContains.add(currentTimeZone);
                        }
                    }
                }

				// last publishing
				// 두 컨테이너를 합치기 전에 priorty 로 서로 정렬
				Collections.sort(sameTimeZoneStartWith, comparator);
				Collections.sort(sameTimeZoneContains, comparator);
//				ArrayList<MNTimeZone> filteredTimeZones = (ArrayList<MNTimeZone>) sameTimeZoneStartWith.clone();
//				filteredTimeZones.addAll(sameTimeZoneStartWith.size(), sameTimeZoneContains);
                sameTimeZoneStartWith.addAll(sameTimeZoneContains);

                return sameTimeZoneStartWith;
//				publishProgress(filteredTimeZones);
			}
		}
		return null;
	}

    /*
	@Override
	protected void onProgressUpdate(Object... values) {

		if (values.length == 1) {
			ArrayList<TimeZoneCity> tempBuffer = (ArrayList<TimeZoneCity>) values[0];
			
			for (int i = 0; i < tempBuffer.size(); ++i)
				m_Adapter.addTimeZone(tempBuffer.get(i));
		}

		m_Adapter.notifyDataSetChanged();

		if( m_Adapter.getCount() == 1 )
		{
			((WorldClockModalActivity) m_Context).text_no_search_result.setAlpha(1);
			((WorldClockModalActivity) m_Context).text_no_search_result.setText(
					m_Context.getResources().getString(R.string.searching)
					);
		}
		else
		{
			((WorldClockModalActivity) m_Context).text_no_search_result.setAlpha(0);
		}
	}
	*/

	@Override
	protected void onCancelled() {
		super.onCancelled();

        /*
		m_Adapter.clear();
		m_Adapter.notifyDataSetChanged();

		((WorldClockModalActivity) m_Context).searchThread = null;
		((WorldClockModalActivity) m_Context).onTextChanged(null, 0, 0, 0);
		*/
	}
	

	@Override
	protected void onPostExecute(ArrayList<MNTimeZone> filteredTimeZones) {
		super.onPostExecute(filteredTimeZones);

        if (listener != null) {
            listener.onTimeZoneSearchFinished(filteredTimeZones);
        }

        /*
		((WorldClockModalActivity) m_Context).searchThread = null;

		m_Adapter.notifyDataSetChanged();
		
		if( m_Adapter.getCount() == 1 )
		{
			((WorldClockModalActivity) m_Context).text_no_search_result.setAlpha(1);
			((WorldClockModalActivity) m_Context).text_no_search_result.setText(
					m_Context.getResources().getString(R.string.no_search_result)
					);
		}
		else
		{
			((WorldClockModalActivity) m_Context).text_no_search_result.setAlpha(0);
		}
		*/
	}
}
