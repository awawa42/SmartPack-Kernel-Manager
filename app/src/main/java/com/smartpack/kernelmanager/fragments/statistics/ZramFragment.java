/*
 * Copyright (C) 2015-2016 Willi Ye <williye97@gmail.com>
 *
 * This file is part of Kernel Adiutor.
 *
 * Kernel Adiutor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kernel Adiutor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kernel Adiutor.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.smartpack.kernelmanager.fragments.statistics;

import com.smartpack.kernelmanager.R;
import com.smartpack.kernelmanager.fragments.DescriptionFragment;
import com.smartpack.kernelmanager.fragments.RecyclerViewFragment;
import com.smartpack.kernelmanager.utils.kernel.vm.ZRAM;
import com.smartpack.kernelmanager.views.recyclerview.CardView;
import com.smartpack.kernelmanager.views.recyclerview.DescriptionView;
import com.smartpack.kernelmanager.views.recyclerview.RecyclerViewItem;

import java.util.List;

/**
 * Created by willi on 28.04.16.
 */
public class ZramFragment extends RecyclerViewFragment {

        @Override
        protected void init() {
                super.init();
        }

        @Override
        protected boolean showViewPager() {
                return false;
        }

        @Override
        protected void addItems(List<RecyclerViewItem> items) {
                CardView zramWBCard = new CardView(getActivity());
                zramWBCard.setTitle(getString(R.string.zram_wb_card));

                if (ZRAM.hasBACKINGDEV()) {

                        DescriptionView backingDev = new DescriptionView();
                        backingDev.setTitle(getString(R.string.backing_dev));
                        backingDev.setSummary(ZRAM.getBACKINGDEV());
                        zramWBCard.addItem(backingDev);

                        List<Long> bdstatList = ZRAM.getBDStat();
                        if (bdstatList.size() >= 3) {
                                String[][] bdStats = {
                                                { getString(R.string.bd_count),
                                                                String.format("%.2f", bdstatList.get(0) * 4.0 / 1024.0)
                                                                                + " " + getString(R.string.mb) },
                                                { getString(R.string.bd_reads),
                                                                String.format("%.2f", bdstatList.get(1) * 4.0 / 1024.0)
                                                                                + " " + getString(R.string.mb) },
                                                { getString(R.string.bd_writes),
                                                                String.format("%.2f", bdstatList.get(2) * 4.0 / 1024.0)
                                                                                + " " + getString(R.string.mb) },
                                };
                                for (String[] bdStat : bdStats) {
                                        if (bdStat[1] == null || bdStat[1].isEmpty()) {
                                                continue;
                                        }
                                        DescriptionView stat = new DescriptionView();
                                        stat.setTitle(bdStat[0]);
                                        stat.setSummary(bdStat[1]);
                                        zramWBCard.addItem(stat);
                                }
                        }
                        items.add(zramWBCard);
                }

                CardView zramCard = new CardView(getActivity());
                zramCard.setTitle(getString(R.string.zram));
                List<Long> statList = ZRAM.getStat();
                List<Long> mmStatList = ZRAM.getMMStat();
                List<Long> ioStatList = ZRAM.getIOStat();
                String[][] zramStats = {
                                { getString(R.string.disksize),
                                                String.format("%d", ZRAM.getDisksize()) + " "
                                                                + getString(R.string.mb) },
                                { getString(R.string.original_data_size),
                                                String.format("%.2f", mmStatList.get(0) / 1024.0 / 1024.0) + " "
                                                                + getString(R.string.mb) },
                                { getString(R.string.memory_used_total),
                                                String.format("%.2f", mmStatList.get(2) / 1024.0 / 1024.0) + " "
                                                                + getString(R.string.mb) },
                                { getString(R.string.compression_ratio),
                                                mmStatList.get(0) == 0 ? "N/A"
                                                                : String.format("%.2f %%", mmStatList.get(2)
                                                                                / (double) mmStatList.get(0) * 100) },
                                { getString(R.string.zram_reads),
                                                String.format("%.2f",
                                                                (statList.size() > 2 ? statList.get(2)
                                                                                : 0) / 2.0 / 1024.0)
                                                                + " "
                                                                + getString(R.string.mb) },
                                { getString(R.string.zram_writes),
                                                String.format("%.2f",
                                                                (statList.size() > 6 ? statList.get(6)
                                                                                : 0) / 2.0 / 1024.0)
                                                                + " "
                                                                + getString(R.string.mb) },
                                { getString(R.string.failed_reads),
                                                String.format("%d", ioStatList.size() > 0 ? ioStatList.get(0) : 0) },
                                { getString(R.string.failed_writes),
                                                String.format("%d", ioStatList.size() > 1 ? ioStatList.get(1) : 0) }
                };

                for (String[] zramStat : zramStats) {
                        if (zramStat[1] == null || zramStat[1].isEmpty()) {
                                continue;
                        }
                        DescriptionView stat = new DescriptionView();
                        stat.setTitle(zramStat[0]);
                        stat.setSummary(zramStat[1]);
                        zramCard.addItem(stat);
                }
                items.add(zramCard);

        }

}
